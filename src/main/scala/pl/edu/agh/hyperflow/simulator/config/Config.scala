package pl.edu.agh.hyperflow.simulator.config

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.container.containerProvisioners.{ContainerBwProvisionerSimple, ContainerPe, ContainerRamProvisionerSimple, CotainerPeProvisionerSimple}
import org.cloudbus.cloudsim.container.containerVmProvisioners.{ContainerVmBwProvisioner, ContainerVmBwProvisionerSimple, ContainerVmPe, ContainerVmPeProvisioner, ContainerVmPeProvisionerSimple, ContainerVmRamProvisionerSimple}
import org.cloudbus.cloudsim.container.schedulers.{ContainerCloudletScheduler, ContainerCloudletSchedulerDynamicWorkload, ContainerScheduler, ContainerSchedulerTimeSharedOverSubscription, ContainerVmSchedulerTimeSharedOverSubscription}
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040

import collection.mutable._

object Config {
  private val config = ConfigFactory.load()
  private val hyperflow = config.getConfig("hyperflow.simulator")
  private val http = config.getConfig("http")
  private val akka = config.getConfig("akka")


  object HyperflowSimulator {
    val appName: String = hyperflow.getString("appName")
  }

  object Http {
    val port: Int = http.getInt("port")
  }

  object Cloud {

    object Host {
      val ram = new ContainerVmRamProvisionerSimple(1000)
      val bandWidth = new ContainerVmBwProvisionerSimple(100000)
      val peList = scala.collection.JavaConverters.seqAsJavaList((1 to 3) map (i => new ContainerVmPe(i, new ContainerVmPeProvisionerSimple(2000.0))))
      val scheduler = new ContainerVmSchedulerTimeSharedOverSubscription(this.peList)
      val powerModel = new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
    }

    object VM {
      val schedulingInterval: Double = 10
      val vmm: String = "xen"
      val size: Long = 100000
      val bw: Long = 100000
      val ram: Float = 1000
      val mips: Double = 2000.0
      val ramProvisioner = new ContainerRamProvisionerSimple(1000)
      val bandWidthProvisioner = new ContainerBwProvisionerSimple(100000)
      val peList = scala.collection.JavaConverters.seqAsJavaList((1 to 3) map (i => new ContainerPe(i, new CotainerPeProvisionerSimple(2000.0))))
      val containerScheduler = new ContainerSchedulerTimeSharedOverSubscription(this.peList)
      val powerModel = new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
    }
    object Container {

      val pes: Int = 100

      val schedulingInterval: Double = 10
      val vmm: String = "xen"
      val size: Long = 100000
      val bw: Long = 100000
      val ram: Int = 1000
      val mips: Double = 2000.0
      val ramProvisioner = new ContainerRamProvisionerSimple(1000)
      val bandWidthProvisioner = new ContainerBwProvisionerSimple(100000)
      val peList = scala.collection.JavaConverters.seqAsJavaList((1 to 3) map (i => new ContainerPe(i, new CotainerPeProvisionerSimple(2000.0))))
      val scheduler: ContainerCloudletScheduler = new ContainerCloudletSchedulerDynamicWorkload(this.mips, this.pes)
      val powerModel = new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
    }
  }

}
