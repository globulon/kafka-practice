package org.omd.kafka.practice.streams

import cats.data.Kleisli
import cats.effect.Sync
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.StreamsBuilder
import org.omd.kafka.practice.settings._

private[streams] trait Factories {
  final def streamsFrom[F[_] : Sync](b: StreamsBuilder, s: Settings): F[KafkaStreams] =
    Sync[F].delay(new KafkaStreams(b.build(), s.props))

  final def withBuilder[F[_] : Sync](f: StreamsBuilder ⇒ Unit): Kleisli[F, StreamsBuilder, StreamsBuilder] =
    Kleisli[F, StreamsBuilder, Unit](s ⇒ Sync[F].delay(f(s))).tap
}
