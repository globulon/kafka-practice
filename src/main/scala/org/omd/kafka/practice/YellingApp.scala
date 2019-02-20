package org.omd.kafka.practice

import java.util.Properties

import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala._

object YellingApp {
  def main(args: Array[String]): Unit = {
    val builder = new StreamsBuilder()
    builder.stream[String, String](topic = "src-topic").mapValues { s ⇒
      println(s"mapping $s ...")
      s.toUpperCase
    }.to(topic = "out-topic")
    new KafkaStreams(builder.build(), props).start()
    Thread.sleep(600000)
  }



  private def props: Properties = {
    val props = new Properties()
    props.put(APPLICATION_ID_CONFIG, "yelling_app_id")
    props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }
}
