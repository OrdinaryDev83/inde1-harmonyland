package fr.epita.harmonyland

import ackcord._
import ackcord.data._
import ackcord.requests._
import io.github.cdimascio.dotenv.Dotenv
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties
import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, seqAsJavaListConverter}

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
