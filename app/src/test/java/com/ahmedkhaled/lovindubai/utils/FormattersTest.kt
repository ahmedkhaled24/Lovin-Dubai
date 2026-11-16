package com.ahmedkhaled.lovindubai.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {

    @Test
    fun `numbers below 1000 are shown as they are`() {
        val result = formatCount(999)
        assertEquals("999", result)
    }

    @Test
    fun `thousands are shown with K`() {
        val result = formatCount(1_500)
        assertEquals("1.5K", result)
    }

    @Test
    fun `millions are shown with M`() {
        val result = formatCount(2_300_000)
        assertEquals("2.3M", result)
    }

    @Test
    fun `duration under one hour shows minutes and seconds`() {
        val result = formatDuration(330)
        assertEquals("5:30", result)
    }

    @Test
    fun `duration over one hour shows hours too`() {
        val result = formatDuration(3723)
        assertEquals("1:02:03", result)
    }
}