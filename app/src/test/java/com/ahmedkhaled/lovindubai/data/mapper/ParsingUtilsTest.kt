package com.ahmedkhaled.lovindubai.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ParsingUtilsTest {

    @Test
    fun `youtube duration is converted to seconds`() {
        val result = parseIsoDurationToSeconds("PT5M30S")
        assertEquals(330L, result)
    }

    @Test
    fun `live stream duration P0D is zero seconds`() {
        val result = parseIsoDurationToSeconds("P0D")
        assertEquals(0L, result)
    }

    @Test
    fun `invalid duration returns zero instead of crashing`() {
        val result = parseIsoDurationToSeconds("invalid")
        assertEquals(0L, result)
    }

    @Test
    fun `invalid date returns null instead of crashing`() {
        val result = parseIsoDateToMillis("not-a-date")
        assertNull(result)
    }
}