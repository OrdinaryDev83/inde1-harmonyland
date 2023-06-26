package fr.epita.harmonyland

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

import java.util.Properties

object StreamConsumer extends App{
  val props: Properties = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "streamConsumer1")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

  consumer.subscribe(List("principal_topic").asJava)

  // Consumer only the message that are doesn't contain the word "test"
  
}
