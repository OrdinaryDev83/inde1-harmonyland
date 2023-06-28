package fr.epita.harmonyland

import com.datastax.spark.connector.SomeColumns
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.datastax.spark.connector.streaming._
import com.google.gson.GsonBuilder

object SparkStreamConsumer{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkStreamConsumer")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .set("spark.sql.extensions", "com.datastax.spark.connector.CassandraSparkExtensions")

    val ssc = new StreamingContext(conf, Seconds(10))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "sparkStreamGroup",
      "auto.offset.reset" -> "latest"
    )

    val topics = List("global2")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    val gson = new GsonBuilder().setPrettyPrinting().create()
    stream.map(records => records.value())
      .map(x => gson.toJson(x))
      .print()
    // saveToCassandra("harmonystate", "report", SomeColumns("latitude", "longitude"))

    ssc.start()
    ssc.awaitTermination()
  }


}
