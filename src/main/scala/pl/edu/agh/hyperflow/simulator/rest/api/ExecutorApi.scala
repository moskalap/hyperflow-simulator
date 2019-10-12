package pl.edu.agh.hyperflow.simulator.rest.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable._
import spray.json.{DefaultJsonProtocol, JsValue, JsonReader, RootJsonReader}
import pl.edu.agh.hyperflow.simulator.services.ExecutorService


class ExecutorApi(executorService: ExecutorService) {

  val routes: Route =
    path("execute") {
      post {
        entity(as[Executable]) { ex =>
          executorService.addExecutable(ex)
          complete(s"OK")
        }
      }


    }


}
