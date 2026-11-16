package com.ahmedkhaled.lovindubai.utils

import java.util.Locale

fun formatCount(count: Long): String = when {
    count >= 1_000_000_000 -> compact(count / 1_000_000_000.0, "B")
    count >= 1_000_000 -> compact(count / 1_000_000.0, "M")
    count >= 1_000 -> compact(count / 1_000.0, "K")
    else -> count.toString()
}

private fun compact(value: Double, suffix: String): String {
    val number = if (value < 10) {
        String.format(Locale.US, "%.1f", value).removeSuffix(".0")
    } else {
        value.toLong().toString()
    }
    return number + suffix
}

fun formatDuration(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%d:%02d", minutes, seconds)
    }
}