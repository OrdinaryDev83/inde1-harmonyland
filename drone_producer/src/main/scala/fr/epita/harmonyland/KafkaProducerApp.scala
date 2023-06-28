package fr.epita.harmonyland

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProducerApp {
  def create_producer() : KafkaProducer[String, String] = {
    val props: Properties = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

    new KafkaProducer[String, String](props)
  }

  def send(producer : KafkaProducer[String, String], json_list : List[String]): Unit = {
    json_list.map((message) => {
      val record = new ProducerRecord[String, String]("global1", "key", message)
      val record2 = new ProducerRecord[String, String]("global2", "key", message)
      producer.send(record)
      producer.send(record2)
      message
    })
  }
}