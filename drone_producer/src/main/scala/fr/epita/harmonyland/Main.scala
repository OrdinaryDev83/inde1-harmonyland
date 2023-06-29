package fr.epita.harmonyland

import net.liftweb.json._
import net.liftweb.json.Serialization.write

import java.util.concurrent.{CountDownLatch, Executors, TimeUnit}

object Main extends App {

  val producer = KafkaProducerApp.create_producer()

  private val scheduler = Executors.newScheduledThreadPool(1)

  scheduler.scheduleAtFixedRate(
    new Runnable {
      override def run(): Unit = {
        implicit val formats: DefaultFormats.type = DefaultFormats
        val json_list = List.range(0, 100)
          .map(Simulation.generateReport)
          .map((report) => {
            val json = write(report)
            json
          })
        KafkaProducerApp.send(producer, json_list)
      }
    },
    0,
    60,
    TimeUnit.SECONDS
  )

  val latch = new CountDownLatch(1)
  latch.await()

  scheduler.shutdown()
}
