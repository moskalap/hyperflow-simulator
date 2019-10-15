package pl.edu.agh.hyperflow.simulator.services

import java.util
import java.util.Calendar

import pl.edu.agh.hyperflow.simulator.config.Config.Cloud
import akka.event.LoggingAdapter
import org.cloudbus.cloudsim.{Cloudlet, DatacenterBroker, UtilizationModelFull, UtilizationModelNull}
import org.cloudbus.cloudsim.container.core.{Container, ContainerCloudlet, ContainerDatacenterBroker, ContainerDatacenterCharacteristics, ContainerHost, PowerContainer, PowerContainerDatacenterCM, PowerContainerHostUtilizationHistory, PowerContainerVm}
import org.cloudbus.cloudsim.container.resourceAllocatorMigrationEnabled.PowerContainerVmAllocationPolicyMigrationAbstractHostSelection
import org.cloudbus.cloudsim.container.utils.IDs
import org.cloudbus.cloudsim.core.CloudSim
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable.Executable


class ExecutorService(implicit logger: LoggingAdapter) {

  def addExecutable(ex: Executable) = {
    logger.info(s"add executable ${ex.toString}")

    val newCloudlet = new ContainerCloudlet(ex.procId, Math.random().toInt, 0, 0, 0, utlizationNull, utlizationNull, utlizationNull, false)
    val cloudlets = new util.ArrayList[ContainerCloudlet]()
    broker.submitCloudletList(cloudlets)
  }


  logger.info("Initializing executor service")
  CloudSim.init(1, Calendar.getInstance(), true)


  val broker = new ContainerDatacenterBroker("broker", Cloud.Policies.overUtilizationThreshold)
  val hosts = this.createHosts(1)
  val vms = this.createVms(1)
  val containers = this.createContainers(1)
  val datacenter = createDatacenter()


  val utlizationNull = new UtilizationModelFull
  val initCloudlet = new ContainerCloudlet(IDs.pollId(classOf[ContainerCloudlet]), 1000000000, 100, 100, 10, utlizationNull, utlizationNull, utlizationNull, true)
  val initCloudlet1 = new ContainerCloudlet(IDs.pollId(classOf[ContainerCloudlet]), 1000000000, 100, 100, 10, utlizationNull, utlizationNull, utlizationNull, true)
  val cloudlets = new util.ArrayList[ContainerCloudlet]()
  cloudlets.add(initCloudlet)
  cloudlets.add(initCloudlet1)
  broker.submitCloudletList(cloudlets)
  broker.submitContainerList(containers)
  broker.submitVmList(vms)


  CloudSim.startSimulation()

  def createDatacenter() = {
    val datacenterCharacteristics = new ContainerDatacenterCharacteristics(
      Cloud.Datacenter.architecture,
      Cloud.Datacenter.os,
      Cloud.Datacenter.vmm,
      this.hosts,
      Cloud.Datacenter.timezone,
      Cloud.Datacenter.costPerSec,
      Cloud.Datacenter.costPerMem,
      Cloud.Datacenter.costPerStorage,
      Cloud.Datacenter.costPerBw
    )

    new PowerContainerDatacenterCM(
      Cloud.Datacenter.name,
      datacenterCharacteristics,
      new PowerContainerVmAllocationPolicyMigrationAbstractHostSelection(hosts, Cloud.Policies.vm, Cloud.Policies.host, Cloud.Policies.overUtilizationThreshold, Cloud.Policies.underUtilizationThreshold),
      Cloud.Policies.container,
      Cloud.Datacenter.storageList,
      Cloud.Datacenter.schedulingInterval,
      Cloud.Datacenter.experimentName,
      Cloud.Datacenter.logAddres,
      Cloud.Datacenter.vmStartupDelay,
      Cloud.Datacenter.containerStartupDelay
    )
  }


  //  val datacenter = createDatacenter()

  def createHosts(numberOfHost: Int) = {
    scala.collection.JavaConverters.seqAsJavaList((1 to numberOfHost) map (i => new PowerContainerHostUtilizationHistory(i, Cloud.Host.ram, Cloud.Host.bandWidth, 1000000, Cloud.Host.peList, Cloud.Host.scheduler, Cloud.Host.powerModel)))
  }

  def createVms(numberOfVms: Int) = {
    scala.collection.JavaConverters.seqAsJavaList((1 to numberOfVms) map (i => new PowerContainerVm(
      i,
      this.broker.getId,
      Cloud.VM.mips,
      Cloud.VM.ram,
      Cloud.VM.bw,
      Cloud.VM.size,
      Cloud.VM.vmm,
      Cloud.VM.containerScheduler,
      Cloud.VM.ramProvisioner,
      Cloud.VM.bandWidthProvisioner,
      Cloud.VM.peList,
      Cloud.VM.schedulingInterval)))
  }

  def createContainers(numberOfContainers: Int) = {
    scala.collection.JavaConverters.seqAsJavaList((1 to numberOfContainers) map (i => new PowerContainer(
      IDs.pollId(classOf[Container]),
      this.broker.getId,
      Cloud.Container.mips,
      Cloud.Container.pes,
      Cloud.Container.ram,
      Cloud.Container.bw,
      Cloud.Container.size,
      Cloud.Container.vmm,
      Cloud.Container.scheduler,
      Cloud.Container.schedulingInterval,

    )))
  }
}
