name := "Alert Stream Consumer"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "2.3.0",
  "org.apache.kafka" % "kafka-streams" % "2.3.0",
  "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0"
)

