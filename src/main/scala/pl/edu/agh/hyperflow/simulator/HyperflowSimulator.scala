package pl.edu.agh.hyperflow.simulator

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.handleExceptions
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{handleExceptions, _}
import akka.stream.ActorMaterializer
import config.Config
import pl.edu.agh.hyperflow.simulator.rest.api.ExecutorApi
import pl.edu.agh.hyperflow.simulator.services.ExecutorService

import scala.concurrent.duration._
import scala.concurrent.Await
object HyperflowSimulator extends App {
 implicit val system = ActorSystem(Config.HyperflowSimulator.appName)
 implicit val mat = ActorMaterializer()(system)
 implicit val logger: LoggingAdapter = Logging(system, this.getClass)

 logger.info(s"${Config.HyperflowSimulator.appName} is starting...")

 scala.sys.addShutdownHook {
  system.terminate()
  Await.result(system.whenTerminated, 5.seconds)
 }
 val executorService = new ExecutorService()
 val executorApi = new ExecutorApi(executorService)

 Http()(system).bindAndHandle(
  executorApi.routes,
  "0.0.0.0",
  8080,
 )
}
