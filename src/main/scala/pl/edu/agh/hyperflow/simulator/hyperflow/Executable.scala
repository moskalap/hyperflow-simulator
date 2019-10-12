package pl.edu.agh.hyperflow.simulator.hyperflow

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsObject, JsValue, RootJsonReader}
import pl.edu.agh.hyperflow.simulator.rest.api.SprayUtils._

object Executable extends SprayJsonSupport with DefaultJsonProtocol {

  final case class Executor(name: String, args: List[String])
  final case class Executable(workflowId: Int, name: String, procId: Int, executor: Executor)
  implicit val executableReader: RootJsonReader[Executable] = (json: JsValue) => {
    val jsObj = json.asJsObject
    val executor = jsObj.get[JsObject]("executor")

    Executable(
      jsObj.get[Int]("workflowId"),
      jsObj.get[String]("name"),
      jsObj.get[Int]("procId"),
        Executor(executor.get[String]("executable"), executor.get[List[String]]("args") )
    )
  }

}
