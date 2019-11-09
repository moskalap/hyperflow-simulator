package pl.edu.agh.hyperflow.simulator.services.execution

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, OverflowStrategy}
import pl.edu.agh.hyperflow.simulator.config.Config
import pl.edu.agh.hyperflow.simulator.hyperflow.Executable.Executable
import redis.clients.jedis.Jedis

import scala.concurrent.duration._


class ExecutorService(implicit logger: LoggingAdapter) {
  def stop() = {
    logger.info("STOPP")
    simulation.stop()
  }

  val simulation = new CloudSimulation()

  implicit val system = ActorSystem("executor")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system))


  val queue = Source
    .queue[Executable](1000, OverflowStrategy.dropBuffer)
    .groupedWithin(100, 100.millisecond)
    .toMat(Sink.foreach(x => {
      logger.info(s"submiting ${x.size} tasks")
      simulation.submitTasks(x)
    }))(Keep.left)
    .run()


  def addExecutable(ex: Executable): Unit = {
    logger.debug(s"add executable ${ex.toString}")
    queue.offer(ex)
  }

}
