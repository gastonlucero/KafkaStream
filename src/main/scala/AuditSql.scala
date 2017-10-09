import java.lang.Long
import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder, KTable}

import scala.collection.JavaConverters.asJavaIterableConverter

object AuditSql extends App {

  val streamingConfig = {
    val settings = new Properties
    settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-function-scala-example")
    settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass.getName)
    settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    settings
  }

  val builder: KStreamBuilder = new KStreamBuilder

  val config = new StreamsConfig(streamingConfig)
  val textLines: KStream[String, String] = builder.stream("input-topic")
  val wordCounts: KTable[String, Long] =
    textLines
      .flatMapValues(textLine => {
        textLine.toLowerCase.split("\\W+").toIterable.asJava
      })
      .groupBy((_, word) => {
        println(word)
        word
      })
      .count("Counts")
  wordCounts.to(Serdes.String(), Serdes.Long(), "output-topic")


  val streams: KafkaStreams = new KafkaStreams(builder, config)
  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    streams.close(10, TimeUnit.SECONDS)
  }))
}
