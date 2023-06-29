name := "Alert Stream Consumer"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "org.scala-lang" % "scala-reflect" % "2.11.12",
  "org.apache.spark" %% "spark-core" % "2.4.8",
  "org.apache.spark" %% "spark-streaming" % "2.4.8",
  "org.apache.spark" %% "spark-sql" % "2.4.8",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.8",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.2",
)

assembly / assemblyJarName := "spark-streaming-fatjar-1.0.jar"

assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
}
