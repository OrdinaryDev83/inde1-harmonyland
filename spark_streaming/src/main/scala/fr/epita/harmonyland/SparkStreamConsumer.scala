package fr.epita.harmonyland

import com.datastax.spark.connector.CassandraSparkExtensions
import com.datastax.spark.connector.types.TupleType
import org.apache.spark.SparkConf
import org.apache.spark.sql.cassandra.DataFrameWriterWrapper
import org.apache.spark.sql.functions.{col, collect_list, explode, from_json, struct, typedLit, udf}
import org.apache.spark.sql.{Dataset, Encoders, Row, SparkSession}

import java.sql.Date

object SparkStreamConsumer{
  case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Date) extends Product with Serializable
  case class Person(firstname: String, lastname: String, harmonyscore: Int) extends Product with Serializable

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
      .option("subscribe", "global2")
      .load()

    val reportSchema = Encoders.product[Report].schema
    val streamDataFrame = df.select(from_json(col("value").cast("string"), reportSchema)
      .alias("value"))
      .select("value.*")
      .withColumnRenamed("droneId", "droneid")
      .filter(col("droneid").isNotNull)
      .filter(col("time").isNotNull)

    val reportDataFrame = streamDataFrame
      .select("droneid", "longitude", "latitude", "persons", "words", "time")

    val reportQuery = reportDataFrame.writeStream
      .foreachBatch { (batchDF : Dataset[Row], batchId : Long ) =>
        batchDF.write
          .cassandraFormat("report", "harmonystate")
          .mode("append")
          .save()
      }
      .start()

    reportQuery.awaitTermination()
  }
}
