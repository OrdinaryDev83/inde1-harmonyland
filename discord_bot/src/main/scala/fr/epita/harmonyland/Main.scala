package fr.epita.harmonyland

import ackcord._
import io.github.cdimascio.dotenv.Dotenv

import java.util.concurrent.{CountDownLatch, Executors, TimeUnit}
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.sys.env

object Main extends App {
  // load .env file
  val dotenv = Dotenv.configure().ignoreIfMissing().load()
  val token = dotenv.get("TOKEN")
  val clientSettings = ClientSettings(token)
  //The client settings contains an execution context that you can use before you have access to the client
  //import clientSettings.executionContext

  //In real code, please dont block on the client construction
  val client = Await.result(clientSettings.createClient(), Duration(10, SECONDS))

  //The client also contains an execution context
  //import client.executionContext

  client.login()
  println("[HARMONYSOFT] Logged in")
  Actions.sendMessage(dotenv, client, ":green_circle: Online")
  println("[HARMONYSOFT] Online")
}