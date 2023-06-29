package fr.epita.harmonyland

import org.apache.spark.sql.SparkSession
import com.datastax.spark.connector._
import org.apache.spark.SparkConf

object CassandraBatchProcessing {
  def main(args: Array[String]): Unit = {
    // Create a SparkSession
    val config = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkStreamConsumer")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .set("spark.sql.extensions", "com.datastax.spark.connector.CassandraSparkExtensions")

    val spark = SparkSession.builder
      .config(config)
      .withExtensions(new CassandraSparkExtensions)
      .getOrCreate()

    val df = spark.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "report", "keyspace" -> "harmonystate"))
      .load()

    val explodedDf = DataFrameTransformation.explodePersons(df)
    val averageScores = DataFrameTransformation.getAverageHarmonyScores(explodedDf)
    val personPerTime = DataFrameTransformation.personPerTime(explodedDf)
    val badHarmonyScorePerTime = DataFrameTransformation.badHarmonyPerTime(explodedDf)
    val repeatOffender = DataFrameTransformation.repeatOffender(explodedDf)

    repeatOffender.show(10)

    averageScores.write
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "harmonyscores", "keyspace" -> "harmonystate"))
      .mode("append")
      .save()

    personPerTime.write
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "persons", "keyspace" -> "harmonystate"))
      .mode("append")
      .save()

    badHarmonyScorePerTime.write
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "badpersons", "keyspace" -> "harmonystate"))
      .mode("append")
      .save()

    spark.stop()
  }
}
