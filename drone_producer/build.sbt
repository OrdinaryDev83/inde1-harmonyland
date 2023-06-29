name := "Drone Producer"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "net.liftweb" %% "lift-json" % "3.4.3"
)

