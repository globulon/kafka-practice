package org.omd.kafka.practice.streams

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.Grouped

private[streams] trait DefaultGrouped {
  implicit final def grouped[K: Serde, V: Serde]: Grouped[K, V] = Grouped.`with`[K, V]
}
