package com.vazdautsan.pocketinbox.feature.notes

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/** `29.10.2026 21:57` */
private val NoteDateFormat = LocalDateTime.Format {
    day()
    char('.')
    monthNumber()
    char('.')
    year()
    char(' ')
    hour()
    char(':')
    minute()
}

internal fun formatNoteDate(
    instant: Instant,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String = instant.toLocalDateTime(timeZone).format(NoteDateFormat)
