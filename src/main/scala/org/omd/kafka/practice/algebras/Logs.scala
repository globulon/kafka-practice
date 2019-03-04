package org.omd.kafka.practice.algebras

import org.omd.kafka.practice.streams.Topic

trait Logs[M[_], Store[_, _], K, V] {
  def from: Topic ⇒ M[Store[K, V]]

  def to: Store[K, V] ⇒ Topic ⇒ M[Unit]
}
