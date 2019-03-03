package org.omd.kafka.practice.streams

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}

private[kafka] trait DefaultSerdes {
  implicit final def consumed[K: Serde, V: Serde]: Consumed[K, V] = Consumed.`with`[K, V]

  implicit final def produced[K: Serde, V: Serde]: Produced[K, V] = Produced.`with`[K, V]
}
