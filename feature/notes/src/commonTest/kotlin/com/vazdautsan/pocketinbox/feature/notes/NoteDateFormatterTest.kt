package com.vazdautsan.pocketinbox.feature.notes

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class NoteDateFormatterTest {

    @Test
    fun formatsInGivenTimeZoneWithZeroPadding() {
        val instant = Instant.parse("2026-01-05T06:07:00Z")

        assertEquals("05.01.2026 06:07", formatNoteDate(instant, TimeZone.UTC))
        assertEquals("05.01.2026 09:07", formatNoteDate(instant, TimeZone.of("Europe/Moscow")))
    }
}
