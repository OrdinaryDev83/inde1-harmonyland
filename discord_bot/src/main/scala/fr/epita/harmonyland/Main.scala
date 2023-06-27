package fr.epita.harmonyland

import ackcord._
import ackcord.data.{ChannelId, GuildId, NormalTextGuildChannel, TextChannelId, TextGuildChannel}
import ackcord.interactions.InteractionsRegistrar
import ackcord.requests.{CreateMessage, CreateMessageData}
import ackcord.syntax.{ChannelSyntax, GatewayGuildSyntax, TextChannelSyntax}
import io.github.cdimascio.dotenv.Dotenv

import scala.concurrent.{Await, ExecutionContext}
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