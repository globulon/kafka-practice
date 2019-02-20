package org.omd.kafka.practice.settings

import java.util.Properties

import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG}

private[settings] trait Translations {
  final implicit def settingsOps(s: Settings): SettingsOps = new SettingsOps(s)
}

final class SettingsOps(val s: Settings) extends AnyVal {
  def props: Properties = {
    val props = new Properties()
    props.put(APPLICATION_ID_CONFIG, s.appId)
    props.put(BOOTSTRAP_SERVERS_CONFIG, s.bootstrap)
    props
  }
}
