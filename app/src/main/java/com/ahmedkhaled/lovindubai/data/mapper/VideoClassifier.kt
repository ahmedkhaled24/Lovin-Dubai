package com.ahmedkhaled.lovindubai.data.mapper

import com.ahmedkhaled.lovindubai.domain.models.LiveStatus
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import java.time.Instant

data class VideoClassification(
    val type: VideoType,
    val liveStatus: LiveStatus?
)

object VideoClassifier {

    private const val LEGACY_SHORT_MAX_SECONDS = 60L
    private const val SHORT_MAX_SECONDS = 180L
    private val THREE_MINUTE_SHORTS_START =
        Instant.parse("2024-10-15T00:00:00Z").toEpochMilli()

    fun classify(
        liveBroadcastContent: String?,
        hasLiveStreamingDetails: Boolean,
        durationSeconds: Long,
        publishedAtMillis: Long
    ): VideoClassification {
        val liveStatus = liveStatusOf(liveBroadcastContent, hasLiveStreamingDetails)
        if (liveStatus != null) {
            return VideoClassification(VideoType.LIVE, liveStatus)
        }

        val type =
            if (isShort(durationSeconds, publishedAtMillis))
                VideoType.SHORT
            else
                VideoType.REGULAR
        return VideoClassification(type, null)
    }

    private fun liveStatusOf(
        liveBroadcastContent: String?,
        hasLiveStreamingDetails: Boolean
    ): LiveStatus? =
        when (liveBroadcastContent) {
            "live" -> LiveStatus.ACTIVE
            "upcoming" -> LiveStatus.UPCOMING
            else ->
                if (hasLiveStreamingDetails)
                    LiveStatus.COMPLETED
                else
                    null
        }

    private fun isShort(durationSeconds: Long, publishedAtMillis: Long): Boolean {
        if (durationSeconds <= 0) return false
        val maxSeconds =
            if (publishedAtMillis >= THREE_MINUTE_SHORTS_START)
                SHORT_MAX_SECONDS
            else
                LEGACY_SHORT_MAX_SECONDS

        return durationSeconds <= maxSeconds
    }
}