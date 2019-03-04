package org.omd.kafka.practice.interpreters

import cats.effect.Sync
import cats.implicits._
import cats.mtl.MonadState
import cats.mtl.implicits._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}
import org.omd.kafka.practice.algebras.Logs
import org.omd.kafka.practice.streams._

private[interpreters] trait StreamBuilders {
  final def logs[M[_] : Sync, Store[_, _], K, V](implicit
                                                 C: Consumed[K, V],
                                                 P: Produced[K, V],
                                                 MS: MonadState[M, StreamsBuilder],
                                                 BS: BuildLogs[M, Store]): Logs[M, Store, K, V] =
    new Logs[M, Store, K, V] {
      final def from: Topic ⇒ M[Store[K, V]] = t ⇒ for {
        sb ← MS.get
        st ← BS.read[K, V].apply(t)(sb)
      } yield st

      override def to: Store[K, V] ⇒ Topic ⇒ M[Unit] = s ⇒ BS.persist[K, V].apply(_)(s)
    }

}