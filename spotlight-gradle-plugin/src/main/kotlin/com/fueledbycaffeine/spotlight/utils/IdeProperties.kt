package com.fueledbycaffeine.spotlight.utils

import org.gradle.api.initialization.Settings
import org.gradle.api.provider.ProviderFactory

private const val PROP_SYNC_ACTIVE = "idea.sync.active"

private val ProviderFactory.isIdeSync: Boolean
  get() = systemProperty(PROP_SYNC_ACTIVE)
    .getOrElse("false").toBoolean()

internal val Settings.isIdeSync get() = providers.isIdeSync