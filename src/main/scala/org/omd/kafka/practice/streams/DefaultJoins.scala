package org.omd.kafka.practice.streams

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.Joined

private[streams] trait DefaultJoins {
  implicit final def joined[K: Serde, V: Serde, VO: Serde]: Joined[K, V, VO] = Joined.`with`[K, V, VO]
}
