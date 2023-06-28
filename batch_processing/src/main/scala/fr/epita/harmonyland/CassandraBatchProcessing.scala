package fr.epita.harmonyland

import org.apache.spark.sql.SparkSession
import com.datastax.spark.connector._
import org.apache.spark.SparkConf

object CassandraBatchProcessing {
  def main(args: Array[String]) {
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

    // Read data from Cassandra
    val df = spark.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "person", "keyspace" -> "harmonystate"))
      .load()
    print(df.show(10))

    // Perform batch processing (transformation)
    val transformedDF = df // replace with your transformation

//    // Write data back to Cassandra
//    transformedDF.write
//      .format("org.apache.spark.sql.cassandra")
//      .options(Map( "table" -> "my_output_table", "keyspace" -> "my_keyspace")) // replace with your output table and keyspace
//      .mode("overwrite")
//      .save()

    spark.stop()
  }
}
