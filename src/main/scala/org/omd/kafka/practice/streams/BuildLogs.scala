package org.omd.kafka.practice.streams

import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}

import scala.annotation.implicitNotFound

@implicitNotFound(msg = "No BuildStore instance found for effect ${M} and ${Store}")
trait BuildLogs[M[_], Store[_, _]] {
  def read[K, V](implicit C: Consumed[K, V]): Topic ⇒ StreamsBuilder ⇒ M[Store[K, V]]

  def persist[K, V](implicit C: Produced[K, V]): Topic ⇒ Store[K, V] ⇒ M[Unit]
}

object BuildLogs {
  def apply[M[_], Store[_, _]](implicit BS: BuildLogs[M, Store]): BuildLogs[M, Store] = BS
}