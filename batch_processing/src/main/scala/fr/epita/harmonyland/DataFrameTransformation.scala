package fr.epita.harmonyland

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, explode, udf}

object DataFrameTransformation {

  // Required by grafana
  val uuid: UserDefinedFunction = udf(() => "9d87a7e3-5d71-4469-97a6-2588943d2e4a")

  def explodePersons(df: DataFrame): DataFrame =
    df.withColumn("person", explode(col("persons")))
      .withColumn("time", col("time").cast("timestamp"))

  // Return a DataFrame with the average harmony score per time
  def getAverageHarmonyScores(df: DataFrame): DataFrame =
    df.select(col("time"), col("person.harmonyscore"))
      .groupBy("time")
      .avg("harmonyscore")
      .sort(col("time").asc)
      .withColumn("avg(harmonyscore)", col("avg(harmonyscore)") * 100.0)
      .withColumn("avg(harmonyscore)", col("avg(harmonyscore)").cast("int"))
      .withColumnRenamed("avg(harmonyscore)", "harmonyscore")
      .withColumn("id", uuid()) // Required by grafana dashboard

  // Return a DataFrame with the number of person per time
  def personPerTime(df: DataFrame): DataFrame =
    df.select(col("time"), col("person.firstname"), col("person.lastname"))
      .groupBy("time").count()
      .sort(col("count").desc)
      .withColumn("id", uuid()) // Required by grafana dashboard

  // Return a DataFrame with the number of person with a bad harmony score per time
  def badHarmonyPerTime(df: DataFrame): DataFrame =
    df.select(col("time"), col("person.firstname"), col("person.lastname"), col("person.harmonyscore"))
      .filter(col("person.harmonyscore") < 2)
      .groupBy("time").count()
      .sort(col("count").desc)
      .withColumn("id", uuid()) // Required by grafana dashboard

  // Return a DataFrame with the number of person per time
  def repeatOffender(df: DataFrame): DataFrame =
    df.select(col("person.firstname"), col("person.lastname"), col("person.harmonyscore"))
      .filter(col("person.harmonyscore") < 2)
      .groupBy("firstname", "lastname")
      .count()
      .sort(col("count").desc)
}
