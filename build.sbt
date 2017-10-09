name := "KafkaStream"

version := "0.1"

scalaVersion := "2.12.3"

val kafkaVersion = "0.11.0.1"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "com.typesafe" % "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)