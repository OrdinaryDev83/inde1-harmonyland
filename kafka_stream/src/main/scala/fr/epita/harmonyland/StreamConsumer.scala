package fr.epita.harmonyland

import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream

import java.time.Duration
import java.util.Properties

object StreamConsumer extends App{
  val config: Properties = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-function-scala-example")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }

  val builder = new StreamsBuilder
  val sources: KStream[String, String] = builder.stream[String, String]("global1")

  val badScorePattern = "\"harmonyscore\":[0|1]"
  val alerts: KStream[String, String] = sources.filter((_, value) => badScorePattern.r.findFirstIn(value).isDefined)
  alerts.to("alert")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), config)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}
