package com.vazdautsan.pocketinbox.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal object ProjectConfig {
    val javaVersion = JavaVersion.VERSION_11
    val jvmTarget = JvmTarget.JVM_11
}
