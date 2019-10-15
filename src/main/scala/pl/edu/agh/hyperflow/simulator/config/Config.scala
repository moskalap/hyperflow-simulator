package pl.edu.agh.hyperflow.simulator.config

import java.util

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.Storage
import org.cloudbus.cloudsim.container.containerProvisioners.{ContainerBwProvisionerSimple, ContainerPe, ContainerRamProvisionerSimple, CotainerPeProvisionerSimple}
import org.cloudbus.cloudsim.container.containerVmProvisioners.{ContainerVmBwProvisioner, ContainerVmBwProvisionerSimple, ContainerVmPe, ContainerVmPeProvisioner, ContainerVmPeProvisionerSimple, ContainerVmRamProvisionerSimple}
import org.cloudbus.cloudsim.container.hostSelectionPolicies.HostSelectionPolicyFirstFit
import org.cloudbus.cloudsim.container.resourceAllocatorMigrationEnabled.PowerContainerVmAllocationPolicyMigrationAbstractHostSelection
import org.cloudbus.cloudsim.container.resourceAllocators.{ContainerVmAllocationPolicy, PowerContainerAllocationPolicySimple}
import org.cloudbus.cloudsim.container.schedulers.{ContainerCloudletScheduler, ContainerCloudletSchedulerDynamicWorkload, ContainerScheduler, ContainerSchedulerTimeSharedOverSubscription, ContainerVmSchedulerTimeSharedOverSubscription}
import org.cloudbus.cloudsim.container.vmSelectionPolicies.{PowerContainerVmSelectionPolicy, PowerContainerVmSelectionPolicyMaximumUsage}
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
    object Policies {
      val container = new PowerContainerAllocationPolicySimple
      val vm: PowerContainerVmSelectionPolicy = new PowerContainerVmSelectionPolicyMaximumUsage
      val host = new HostSelectionPolicyFirstFit
      val overUtilizationThreshold = 0.8
      val underUtilizationThreshold = 0.7
    }

    object Datacenter {
      val containerStartupDelay: Double = 0.1
      val vmStartupDelay: Double = 0.1
      val logAddres: String = "./exp"
      var experimentName: String = "ContainerCloudSimExample_1"
      var schedulingInterval: Double = 10
      val storageList: util.List[Storage] = new util.ArrayList[Storage]()
      val name: String = "datacenter"
      val costPerBw: Double = 0
      val costPerStorage: Double = 0.001
      val costPerMem: Double = 0.05
      val costPerSec: Double = 3.0
      val timezone: Double = 10.0d
      val vmm: String = "Xem"
      val os: String = "Linux"
      val architecture: String = "x86"
    }


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
      val pes: Int = 10
      val schedulingInterval: Double = 10
      val vmm: String = "xen"
      val size: Long = 100
      val bw: Long = 1000
      val ram: Int = 100
      val mips: Double = 200.0
      val ramProvisioner = new ContainerRamProvisionerSimple(100)
      val bandWidthProvisioner = new ContainerBwProvisionerSimple(1000)
      val peList = scala.collection.JavaConverters.seqAsJavaList((1 to 3) map (i => new ContainerPe(i, new CotainerPeProvisionerSimple(2000.0))))
      val scheduler: ContainerCloudletScheduler = new ContainerCloudletSchedulerDynamicWorkload(this.mips, this.pes)
      val powerModel = new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
    }
  }

}
