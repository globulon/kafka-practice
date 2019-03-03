package org.omd.kafka.practice

import cats.data.StateT
import cats.effect.{ExitCode, IO, IOApp, Sync}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.omd.kafka.practice.algebras.Logs
import org.omd.kafka.practice.streams._


object MergeStreams extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = ???

  type Build[A] = StateT[IO, StreamsBuilder, A]

  private def run(program: MergeStreams[Build],
                  clicks: Logs[Build, KStream, String, Long],
                  locations: Logs[Build, KTable, String, String]): Build[KTable[String, Long]] = for {
    clicks ← clicks.from(Topic("user-clicks"))
    locs   ← locations.from(Topic("geo-locations"))
    merged ← program.merge(clicks, locs)
    result ← program.reduce(merged)
  } yield result
}

private[practice] final class MergeStreams[M[_] : Sync]() {
  def merge(clicks: KStream[String, Long], locs: KTable[String, String]): M[KStream[String, (String, Long)]] =
    Sync[M].delay {
      clicks.leftJoin(locs) {
        case (click, loc) if Option(loc).isDefined ⇒ (loc, click)
        case (click, _) ⇒ ("UNKNOWN", click)
      }
    }

  def reduce(joined: KStream[String, (String, Long)]): M[KTable[String, Long]] = Sync[M].delay {
    joined.map {
      case (_, (region, clicks)) ⇒ (region, clicks)
    }.groupByKey.reduce(_ + _)
  }
}
