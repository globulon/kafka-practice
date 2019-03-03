package org.omd.kafka.practice.interpreters

import cats.effect.Sync
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}
import org.omd.kafka.practice.algebras.Logs
import org.omd.kafka.practice.streams._

private[interpreters] trait StreamBuilders {
  final def build[M[_] : Sync, Store[_, _], K, V](b: StreamsBuilder)
                                    (implicit C: Consumed[K, V], P: Produced[K, V],
                                     BS: BuildStore[M, Store]): Logs[M, Store, K, V] =
    new Logs[M, Store, K, V] {
      final def from: Topic ⇒ M[Store[K, V]] = BS.build[K, V].apply(_)(b)

      override def to: Store[K, V] ⇒ Topic ⇒ M[Unit] = s ⇒ BS.persist[K, V].apply(_)(s)
    }
}