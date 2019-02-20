package org.omd.kafka.practice

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}

private[kafka] trait DefaultSerdes {
  implicit def consumed[K : Serde, V : Serde] : Consumed[K, V] = Consumed.`with`[K, V]
  implicit def produced[K : Serde, V : Serde] : Produced[K, V] = Produced.`with`[K, V]

}
