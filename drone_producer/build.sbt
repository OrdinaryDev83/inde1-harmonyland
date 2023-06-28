name := "Drone Producer"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.8",
  "org.apache.spark" %% "spark-sql" % "2.4.8" % "provided",
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "net.liftweb" %% "lift-json" % "3.4.3"
)

