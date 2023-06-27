package fr.epita.harmonyland

import ackcord.EventListenerMessage.findCache
import ackcord._
import ackcord.commands.CommandMessage.findCache
import ackcord.data._
import ackcord.requests._
import ackcord.syntax._
import akka.stream.Client
import fr.epita.harmonyland.Main.{client, dotenv}
import io.github.cdimascio.dotenv.Dotenv

import scala.concurrent.ExecutionContext

object Actions {
  def sendMessage(dotenv : Dotenv, client: DiscordClient, message: String): Unit = {
    client.onEventSideEffects {
      implicit c => {
        case msg: APIMessage.Ready => {
          println("Ready")
          val channelId = TextChannelId(dotenv.get("CHANNEL_ID").toLong)
          val createMessageData = CreateMessageData(message)
          val createMessageRequest = CreateMessage(channelId, createMessageData)
          client.requestsHelper.run(createMessageRequest)
        }
      }
    }
  }
}
