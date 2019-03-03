package org.omd.kafka.practice.streams

import cats.data.StateT
import cats.effect.Sync
import cats.{Applicative, Functor}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, KStream, KTable}

import scala.annotation.implicitNotFound

@implicitNotFound(msg = "No BuildStore instance found for effect ${M} and ${Store}")
trait BuildStore[M[_], Store[_, _]] {
  def build[K, V](implicit C: Consumed[K, V]): StreamsBuilder ⇒ String ⇒ M[Store[K, V]]
}

object BuildStore {
  def apply[M[_], Store[_, _]](implicit BS: BuildStore[M, Store]): BuildStore[M, Store] = BS
}

private[streams] trait BuildStores {
  final type BuildStreams[M[_], A] = StateT[M, StreamsBuilder, A]

  final def buildStreams[M[_] : Applicative, Store[_,_], K, V](f: StreamsBuilder ⇒ M[Store[K, V]]): BuildStreams[M, Store[K, V]] =
    StateT[M, StreamsBuilder, Store[K, V]] { b ⇒ Functor[M].map(f(b))((b, _)) }

  final def buildStore[M[_] : Applicative, Store[_, _], K, V](topic: String)(implicit C: Consumed[K, V], BS: BuildStore[M, Store]): BuildStreams[M, Store[K, V]] =
    buildStreams[M, Store, K, V] { BS.build[K, V].apply(_)(topic) }

  implicit final def buildStream[M[_] : Sync]: BuildStore[M, KStream] = new BuildStore[M, KStream] {
    override def build[K, V](implicit C: Consumed[K, V]): StreamsBuilder ⇒ String ⇒ M[KStream[K, V]] =
      b ⇒ name ⇒ Sync[M].delay(b.stream[K, V](name))
  }

  implicit final def buildTable[M[_] : Sync]: BuildStore[M, KTable] = new BuildStore[M, KTable] {
    override def build[K, V](implicit C: Consumed[K, V]): StreamsBuilder ⇒ String ⇒ M[KTable[K, V]] =
      b ⇒ name ⇒ Sync[M].delay(b.table[K, V](name))
  }
}