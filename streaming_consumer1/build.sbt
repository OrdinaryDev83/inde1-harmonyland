name := "Alert Stream Consumer"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "org.apache.kafka" % "kafka-clients" % "3.2.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

