package pl.edu.agh.hyperflow.simulator.services.execution

import java.util.function.Consumer

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.provisioners.{PeProvisionerSimple, ResourceProvisioner, ResourceProvisionerSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import pl.edu.agh.hyperflow.simulator.cloud.ContainerTask
import pl.edu.agh.hyperflow.simulator.config.Config
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._
import scala.collection.immutable

class CloudSimulation {


  def stop() = {
    simulation.terminate()
    simulation.clock()
    val terminationTime = simulation.clock() + 0.000001

    while (! simulation.isTimeToTerminateSimulationUnderRequest){
      simulation.terminateAt(terminationTime)
    }

  }
  val jedis = new Jedis(Config.Jedis.host)
  val converter = new CloudletTaskConverter()
  val simulation = new CloudSim(0.0000001)
  var t: Thread = new Thread(() => {
    simulation.start()
    val list = broker.getCloudletFinishedList()

    new TableBuilder(list)
      .build()
  })
  val hosts = List.fill(3) {
    createHost()
  }
  var started = false
  val vms = List.fill(7) {
    createVm()
  }

  val datacenter = createDatacenter()
  val broker = new DatacenterBrokerSimple(simulation)
  broker.submitVmList(vms.asJava)
  var vmId = 1

  def createDatacenter() = {
    new DatacenterSimple(simulation, hosts.asJava, new VmAllocationPolicySimple)
  }

  def createHost(): Host = {
    val mips = 1000
    val ram = 2048
    val storage = 10000
    val bw = 10000
    val numberOfPes = 8
    val pes: List[Pe] = List.fill(8) {new PeSimple(mips, new PeProvisionerSimple())}
    new HostSimple(ram, bw, storage, pes.asJava)
      .setRamProvisioner(new ResourceProvisionerSimple())
      .setBwProvisioner(new ResourceProvisionerSimple())
      .setVmScheduler(new VmSchedulerTimeShared())
  }

  def createVm(): Vm = {
    val mips = 1000
    val ram = 512
    val storage = 1000
    val bw = 1000
    val numberOfPes = 2
    new VmSimple(mips, numberOfPes)
      .setRam(ram)
      .setBw(bw)
      .setSize(storage)
      .setCloudletScheduler(new CloudletSchedulerSpaceShared())

    // add id
  }

  def submitTasks(tasks: immutable.Seq[Executable.Executable]): Unit = synchronized {
    val cloudLets = tasks map converter.taskToCloudlet
    cloudLets.foreach(c => c.addOnFinishListener( e =>
      jedis.publish("finished", e.getCloudlet.getId.toString)
    ))

    if (simulation.isRunning) {
      simulation.pause()
      broker.submitCloudletList(cloudLets.asJava)
      simulation.resume()
    } else {
      broker.submitCloudletList(cloudLets.asJava)
      simulation.terminateAt(100000)
      Thread.sleep(100)

      t.start()
      Thread.sleep(100)
    }


  }

}
