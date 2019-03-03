package org.omd.kafka.practice.streams

import org.apache.kafka.streams.kstream.{Printed ⇒ PrintedJ}

object Printed {
  def toSysOut[K, V]: PrintedJ[K, V] = PrintedJ.toSysOut[K, V]
}
