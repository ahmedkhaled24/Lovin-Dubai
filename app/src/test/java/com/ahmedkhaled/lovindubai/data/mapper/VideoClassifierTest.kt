package com.ahmedkhaled.lovindubai.data.mapper

import com.ahmedkhaled.lovindubai.domain.models.LiveStatus
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class VideoClassifierTest {

    private val publishedIn2025 = Instant.parse("2025-06-01T00:00:00Z").toEpochMilli()

    @Test
    fun `video shorter than 3 minutes is a short`() {
        val result = VideoClassifier.classify(
            liveBroadcastContent = "none",
            hasLiveStreamingDetails = false,
            durationSeconds = 45,
            publishedAtMillis = publishedIn2025
        )
        assertEquals(VideoType.SHORT, result.type)
    }

    @Test
    fun `video longer than 3 minutes is a regular video`() {
        val result = VideoClassifier.classify(
            liveBroadcastContent = "none",
            hasLiveStreamingDetails = false,
            durationSeconds = 600,
            publishedAtMillis = publishedIn2025
        )
        assertEquals(VideoType.REGULAR, result.type)
    }

    @Test
    fun `video broadcasting now is an active live stream`() {
        val result = VideoClassifier.classify(
            liveBroadcastContent = "live",
            hasLiveStreamingDetails = true,
            durationSeconds = 0,
            publishedAtMillis = publishedIn2025
        )
        assertEquals(VideoType.LIVE, result.type)
        assertEquals(LiveStatus.ACTIVE, result.liveStatus)
    }

    @Test
    fun `scheduled broadcast is an upcoming live stream`() {
        val result = VideoClassifier.classify(
            liveBroadcastContent = "upcoming",
            hasLiveStreamingDetails = true,
            durationSeconds = 0,
            publishedAtMillis = publishedIn2025
        )
        assertEquals(VideoType.LIVE, result.type)
        assertEquals(LiveStatus.UPCOMING, result.liveStatus)
    }

    @Test
    fun `finished broadcast is a completed live stream`() {
        val result = VideoClassifier.classify(
            liveBroadcastContent = "none",
            hasLiveStreamingDetails = true,
            durationSeconds = 3600,
            publishedAtMillis = publishedIn2025
        )
        assertEquals(VideoType.LIVE, result.type)
        assertEquals(LiveStatus.COMPLETED, result.liveStatus)
    }
}