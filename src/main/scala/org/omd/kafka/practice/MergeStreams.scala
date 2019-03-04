package org.omd.kafka.practice

import cats.data.StateT
import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.implicits._
import cats.mtl.implicits._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.omd.kafka.practice.interpreters._
import org.omd.kafka.practice.streams._


object MergeStreams extends IOApp {
  private type Build[A] = StateT[IO, StreamsBuilder, A]

  override def run(args: List[String]): IO[ExitCode] = ???

  def kafka: IO[StreamsBuilder] =
    interpret(new MergeStreams[Build, String, String]("UNKNOWN")).runS(new StreamsBuilder())

  private def interpret(program: MergeStreams[Build, String, String]): Build[KTable[String, Long]] = for {
    clicks ← logs[Build, KStream, String, Long].from(Topic("user-clicks"))
    locs   ← logs[Build, KTable, String, String].from(Topic("geo-locations"))
    merged ← program.merge(clicks, locs)
    aggr   ← program.reduce(merged)
    sink   ← logs[Build, KTable, String, Long].to(aggr)(Topic("clicks-per-region"))
  } yield sink
}

private[practice] final class MergeStreams[M[_] : Sync, User: Serde, Region: Serde](default: ⇒ Region) {
  def merge(clicks: KStream[User, Long], locs: KTable[User, Region]): M[KStream[User, (Region, Long)]] =
    Sync[M].delay {
      clicks.leftJoin(locs) {
        case (click, loc) if Option(loc).isDefined ⇒ (loc, click)
        case (click, _) ⇒ (default, click)
      }
    }

  def reduce(joined: KStream[User, (Region, Long)]): M[KTable[Region, Long]] = Sync[M].delay {
    joined.map {
      case (_, (region, clicks)) ⇒ (region, clicks)
    }.groupByKey.reduce(_ + _)
  }
}
