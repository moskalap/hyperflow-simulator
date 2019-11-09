package pl.edu.agh.hyperflow.simulator.cloud

import org.cloudbus.cloudsim.cloudlets.CloudletSimple

class ContainerTask(val name: String, val id: Int,  val length: Int, val cpus: Int ) extends CloudletSimple(id, length, cpus) {

}
