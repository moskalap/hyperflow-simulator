package pl.edu.agh.hyperflow.simulator

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, OverflowStrategy, QueueOfferResult}
import pl.edu.agh.hyperflow.simulator.config.Config
import pl.edu.agh.hyperflow.simulator.rest.api.ExecutorApi
import pl.edu.agh.hyperflow.simulator.services.execution.ExecutorService

import scala.concurrent.Await
import scala.concurrent.duration._

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
    8989,
  )
}
