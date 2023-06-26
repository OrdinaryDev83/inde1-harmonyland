package fr.epita.harmonyland

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProducerApp extends App{
  val props: Properties = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[Simulation.Report])

  val producer: KafkaProducer[String, Simulation.Report] = new KafkaProducer[String, Simulation.Report](props)
  val topic = "quickstart-events"

  val record = new ProducerRecord[String, Simulation.Report](topic, "key", Simulation.generateReport(4))
  producer.send(record)
  producer.close()
}
