package pl.edu.agh.hyperflow.simulator.services.execution

import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic
import pl.edu.agh.hyperflow.simulator.cloud.ContainerTask
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable.Executable

class CloudletTaskConverter() {
  val r =  scala.util.Random
  def taskToCloudlet(task: Executable): Cloudlet = {
    val length = if(task.executor.name == "mDiffFit")  20000  else r.nextInt(1000)
    val fileSize = 300
    val outPutSize = 300
    val cpus = 1
    new ContainerTask(task.executor.name, task.executionId, length, cpus)
      .setFileSize(fileSize)
      .setOutputSize(outPutSize)
      .setUtilizationModel(new UtilizationModelStochastic)

  }
}
