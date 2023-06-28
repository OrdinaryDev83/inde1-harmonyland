package fr.epita.harmonyland

import ackcord._
import io.github.cdimascio.dotenv.Dotenv

import java.util.concurrent.{CountDownLatch, Executors, TimeUnit}
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}

object Main extends App {
  // load .env file
  val dotenv = Dotenv.configure().ignoreIfMissing().load()
  val token = dotenv.get("TOKEN")
  val clientSettings = ClientSettings(token)
  //The client settings contains an execution context that you can use before you have access to the client
  //import clientSettings.executionContext

  //In real code, please dont block on the client construction
  val client = Await.result(clientSettings.createClient(), Duration(10, SECONDS))

  client.login()
  println("[HARMONYSOFT] Logged in")

  client.onEventSideEffects {
    implicit c => {
      case msg: APIMessage.GuildCreate => {
        // The client also contains an execution context
        //import client.executionContext
        Actions.sendMessage(msg.cache.current, dotenv, client, ":green_circle: Online")
        println("[HARMONYSOFT] Online")

        val consumer = Actions.build_consumer()

        println("[HARMONYSOFT] Listening to the Alert topic")

        val scheduler = Executors.newScheduledThreadPool(1)
        val task = new Runnable {
          def run(): Unit = {
            Actions.poll(dotenv, client, msg, consumer)
          }
        }
        scheduler.scheduleAtFixedRate(
          task,
          0,
          2,
          TimeUnit.SECONDS
        )
        // Keep the main thread alive
        val latch = new CountDownLatch(1)

        latch.await()

        scheduler.shutdown()
        consumer.close()
      }
    }
  }
}