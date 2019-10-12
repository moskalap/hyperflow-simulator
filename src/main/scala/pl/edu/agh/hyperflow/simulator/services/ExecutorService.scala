package pl.edu.agh.hyperflow.simulator.services

import java.util.Calendar

import pl.edu.agh.hyperflow.simulator.config.Config.Cloud
import akka.event.LoggingAdapter
import ch.qos.logback.classic.Logger
import org.cloudbus.cloudsim.{Cloudlet, DatacenterBroker}
import org.cloudbus.cloudsim.container.core.{Container, ContainerDatacenterCharacteristics, ContainerHost, PowerContainer, PowerContainerHostUtilizationHistory, PowerContainerVm}
import org.cloudbus.cloudsim.container.utils.IDs
import org.cloudbus.cloudsim.core.CloudSim
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable.Executable



class ExecutorService(implicit logger: LoggingAdapter) {
  def addExecutable(ex: Executable) = {
    logger.info(s"add executable ${ex.toString}")
  }


  logger.info("Initializing executor service")
  CloudSim.init(1, Calendar.getInstance(), true)
  val broker = new DatacenterBroker("broker")
  val hosts = this.createHosts(5)
  val vms = this.createVms(10)
  val containers = this.createContainers(20)

//  def createDatacenter() = {
//    val datacenterCharacteristics = new ContainerDatacenterCharacteristics(
//      Cloud.Datacenter.architecture,
//      Cloud.Datacenter.os,
//      Cloud.Datacenter.vmm,
//      this.hosts,
//      Cloud.Datacenter.timezone,
//      Cloud.Datacenter.costPerSec,
//      Cloud.Datacenter.costPerMem,
//      Cloud.Datacenter.costPerStorage,
//      Cloud.Datacenter.costPerBw
//    )
//  }


//  val datacenter = createDatacenter()

  def createHosts(numberOfHost: Int) = {
    (1 to numberOfHost) map (i => new PowerContainerHostUtilizationHistory(i, Cloud.Host.ram, Cloud.Host.bandWidth, 1000000, Cloud.Host.peList, Cloud.Host.scheduler, Cloud.Host.powerModel ))
  }

  def createVms(numberOfVms: Int) = {
    (1 to numberOfVms) map (i => new PowerContainerVm(
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
      Cloud.VM.schedulingInterval))
  }

  def createContainers(numberOfContainers: Int): Unit = {
    (1 to numberOfContainers) map ( i => new PowerContainer(
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
      
    ))
  }

  def createCloudLet(): Unit = {

  }





}
