package fr.epita.harmonyland

import com.datastax.spark.connector.CassandraSparkExtensions
import org.apache.spark.SparkConf
import org.apache.spark.sql.cassandra.DataFrameWriterWrapper
import org.apache.spark.sql.{Encoders, SparkSession}

import java.util.Date

object SparkStreamConsumer{
  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkStreamConsumer")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .set("spark.sql.extensions", "com.datastax.spark.connector.CassandraSparkExtensions")

    val spark = SparkSession.builder
      .config(config)
      .withExtensions(new CassandraSparkExtensions)
      .getOrCreate()

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("startingOffsets", "earliest")
      .option("subscribe", "global1")
      .load()

    case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Date)
    case class Person(firstName: String, lastName: String, harmonyScore: Int) {
      override def toString: String = {
        s"$firstName $lastName: $harmonyScore"
      }
    }

    //val reportSchema = Encoders.product[Report].schema
    df.select("value")

    df.writeStream
      .foreachBatch { (batchDF, batchId) =>
        batchDF.write
          .cassandraFormat("person", "harmonystate")
          .mode("append")
          .save()
      }
      .start()
      .awaitTermination()
  }
}
