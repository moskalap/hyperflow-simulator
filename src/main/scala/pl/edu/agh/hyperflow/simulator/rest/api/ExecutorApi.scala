package pl.edu.agh.hyperflow.simulator.rest.api

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable._
import pl.edu.agh.hyperflow.simulator.services.execution.ExecutorService
import spray.json.{DefaultJsonProtocol, JsValue, JsonReader, RootJsonReader}

class ExecutorApi(executorService: ExecutorService) {

  val routes: Route = concat(
    path("execute") {
      post {
        entity(as[Executable]) { ex =>
          executorService.addExecutable(ex)
          complete(ex.executionId.toString)
        }
      }
    },
    path("stop") {
      get {

          executorService.stop()
          complete("stopped")

      }
    },
  )




}
