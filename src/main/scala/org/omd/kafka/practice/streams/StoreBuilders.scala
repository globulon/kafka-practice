package org.omd.kafka.practice.streams

import cats.data.StateT
import cats.effect.Sync
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, KStream, KTable, Produced}

private[streams] trait StoreBuilders {
  final type BuildStreams[M[_], A] = StateT[M, StreamsBuilder, A]

  implicit final def buildStream[M[_] : Sync]: BuildLogs[M, KStream] = new BuildLogs[M, KStream] {
    override def read[K, V](implicit C: Consumed[K, V]): Topic ⇒ StreamsBuilder ⇒ M[KStream[K, V]] = {
      case Topic(name) ⇒ b ⇒ Sync[M].delay(b.stream[K, V](name))
    }

    override def persist[K, V](implicit C: Produced[K, V]): Topic ⇒ KStream[K, V] ⇒ M[Unit] = {
      case Topic(name) ⇒ ks ⇒ Sync[M].delay(ks.to(name))
    }
  }

  implicit final def buildTable[M[_] : Sync]: BuildLogs[M, KTable] = new BuildLogs[M, KTable] {
    override def read[K, V](implicit C: Consumed[K, V]): Topic ⇒ StreamsBuilder ⇒ M[KTable[K, V]] = {
      case Topic(name) ⇒ b ⇒ Sync[M].delay(b.table[K, V](name))
    }

    override def persist[K, V](implicit C: Produced[K, V]): Topic ⇒ KTable[K, V] ⇒ M[Unit] = {
      case Topic(name) ⇒ kt ⇒ Sync[M].delay(kt.toStream.to(name))
    }
  }
}