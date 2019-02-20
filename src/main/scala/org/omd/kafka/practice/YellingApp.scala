package org.omd.kafka.practice

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala._
import org.omd.kafka.practice.settings._
import org.omd.kafka.practice.streams._
import scala.concurrent.duration._

import scala.language.postfixOps

object YellingApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    IO(streams(Settings("yelling_app_id", "localhost:9092"))).bracket(_.flatMap(execute))(_.flatMap(interrupt) ) map (_ ⇒ ExitCode.Success)

  private def execute(kafkaStreams: KafkaStreams): IO[Unit] = IO(kafkaStreams.start()) *> IO.sleep(duration = 10 minutes)

  private def interrupt(kafkaStreams: KafkaStreams) = IO(kafkaStreams.close())

  private def streams(fromSettings: Settings): IO[KafkaStreams] = for {
    b ← build.run(new StreamsBuilder())
    r ← IO(new KafkaStreams(b.build(), fromSettings.props))
  } yield r

  private def build: Kleisli[IO, StreamsBuilder, StreamsBuilder] = withBuilder[IO] {
    _.stream[String, String](topic = "src-topic").mapValues { _.toUpperCase }.to(topic = "out-topic")
  }
}
