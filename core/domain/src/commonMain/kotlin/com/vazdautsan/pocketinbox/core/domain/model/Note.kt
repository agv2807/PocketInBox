package com.vazdautsan.pocketinbox.core.domain.model

import kotlin.time.Instant

data class Note(
    val id: Long,
    val text: String,
    val createdAt: Instant,
)
