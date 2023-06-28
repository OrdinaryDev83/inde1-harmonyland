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
  def sendMessage(cache : MemoryCacheSnapshot, dotenv: Dotenv, client: DiscordClient, message: String): Unit = {

    val channelId = TextChannelId(dotenv.get("CHANNEL_ID").toLong)
    val createMessageData = CreateMessageData(message)
    val createMessageRequest = CreateMessage(channelId, createMessageData)
    println("Trying to send message")
    client.requestsHelper.run(createMessageRequest)(cache)
  }

  def build_consumer() : KafkaConsumer[String, String] = {
    val props: Properties = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "discordConsumersGroup")

    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(List("alert").asJava)
    consumer
  }

  def poll(dotenv : Dotenv, client : DiscordClient, msg : APIMessage.GuildCreate, consumer : KafkaConsumer[String, String]) : Unit = {
    val records: ConsumerRecords[String, String] = consumer.poll(java.time.Duration.ofSeconds(1))
    records.asScala.foreach(record => {
      val s = s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}"
      Actions.sendMessage(msg.cache.current, dotenv, client, record.value())
      println(s)
    })
    consumer.commitSync()
  }
}
