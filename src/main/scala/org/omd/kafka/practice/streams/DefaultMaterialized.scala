package org.omd.kafka.practice.streams

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.processor.StateStore
import org.apache.kafka.streams.scala.kstream.Materialized

private[streams] trait DefaultMaterialized {
  implicit final def materialized[K: Serde, V: Serde, S <: StateStore]: Materialized[K, V, S] = Materialized.`with`[K, V, S]
}
