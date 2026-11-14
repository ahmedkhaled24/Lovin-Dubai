package com.ahmedkhaled.lovindubai.data.mapper

import java.time.Duration
import java.time.Instant

fun parseIsoDurationToSeconds(value: String?): Long =
    value?.let { runCatching { Duration.parse(it).seconds }.getOrNull() } ?: 0L

fun parseIsoDateToMillis(value: String?): Long? =
    value?.let { runCatching { Instant.parse(it).toEpochMilli() }.getOrNull() }