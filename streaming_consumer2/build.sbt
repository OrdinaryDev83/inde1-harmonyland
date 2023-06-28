name := "Alert Stream Consumer"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "org.apache.spark" %% "spark-core" % "2.4.8",
  "org.apache.spark" %% "spark-streaming" % "2.4.8",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.8",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "com.google.code.gson" % "gson" % "2.7",
)

