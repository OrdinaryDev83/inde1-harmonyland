package fr.epita.harmonyland

import ackcord._
import ackcord.data._
import ackcord.requests._
import io.github.cdimascio.dotenv.Dotenv
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties
import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, seqAsJavaListConverter}

import net.liftweb.json._
import java.util.Date

case class Person(firstname: String, lastname: String, harmonyscore: Int)
case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Date)

object Actions {
  def parseJson(json: String): Report = {
    // LiftWeb requires you to specify the format of the Date fields in your JSON
    implicit val formats = new DefaultFormats {
      override def dateFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    }
    parse(json).extract[Report]
  }

  def messageFromReport(report : Report) : String = {
    def concatPersons(persons : List[Person]) : String = {
      persons.map(p => s"${p.firstname} ${p.lastname} (${p.harmonyscore})").mkString(", ")
    }
    val badScorePersons = concatPersons(report.persons.filter(p => p.harmonyscore < 2))
    val restPersons = concatPersons(report.persons.filter(p => p.harmonyscore >= 2))
    val words = report.words.mkString(", ")
    val pluralS = if (badScorePersons.length > 1) "s" else ""
    s"""
    |:rotating_light: **Alert !**
    |:small_blue_diamond: Drone : ${report.droneId}
    |:small_blue_diamond: Position : ${report.longitude}, ${report.latitude}
    |:small_blue_diamond: Sad person${pluralS} : $badScorePersons
    |:small_blue_diamond: In contact with : $restPersons
    |:small_blue_diamond: Key words : $words
    |:small_blue_diamond: Date : ${report.time}
    """.stripMargin
  }

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
      Actions.sendMessage(msg.cache.current, dotenv, client, messageFromReport(parseJson(record.value())))
      println(s)
    })
    consumer.commitSync()
  }
}
