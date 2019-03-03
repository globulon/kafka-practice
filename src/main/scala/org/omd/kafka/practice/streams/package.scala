package org.omd.kafka.practice

package object streams extends Factories with StoreBuilders
  with DefaultSerdes with DefaultJoins with DefaultGrouped with DefaultMaterialized