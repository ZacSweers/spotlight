package com.fueledbycaffeine.spotlight.utils

import com.fueledbycaffeine.spotlight.SpotlightBuildService
import com.fueledbycaffeine.spotlight.SpotlightBuildService.Companion.NAME
import com.fueledbycaffeine.spotlight.buildscript.GRADLE_PATH_SEP
import com.fueledbycaffeine.spotlight.buildscript.GradlePath
import java.io.FileNotFoundException
import org.gradle.api.initialization.Settings

internal fun Settings.guessProjectsFromTaskRequests(): Set<GradlePath> {
  return startParameter.taskRequests.flatMap { it.args }
    .filter { it.looksLikeAGradlePath }
    .map {
      val path = GradlePath(rootDir, it.projectPathGuess)
      if (!path.hasBuildFile) {
        throw FileNotFoundException("${it.projectPathGuess} is not a project dir")
      }
      path
    }
    .toSet()
}

private val String.looksLikeAGradlePath: Boolean
  get() = contains(GRADLE_PATH_SEP)

private val String.projectPathGuess: String
  get() = GRADLE_PATH_SEP + substringBeforeLast(GRADLE_PATH_SEP).removePrefix(GRADLE_PATH_SEP)

internal fun Settings.include(paths: Iterable<GradlePath>) {
  gradle.sharedServices.registerIfAbsent(NAME, SpotlightBuildService::class.java) {
    it.parameters.enabled.set(isSpotlightEnabled)
    it.parameters.spotlightProjects.set(paths)
  }

  include(paths.map { it.path })
}