package org.omd.kafka.practice

import cats.effect.{ExitCode, IO, IOApp, Sync}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.omd.kafka.practice.streams._


object MergeStreams extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = ???



  private def userClicks[M[_] : Sync]: BuildStreams[M, KStream[String, Long]] =
    buildStore[M, KStream, String, Long](topic = "user-clicks")

  private def geoLocation[M[_] : Sync]: BuildStreams[M, KTable[String, Long]] =
    buildStore[M, KTable, String, Long](topic = "user-clicks")
}
