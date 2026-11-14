package com.ahmedkhaled.lovindubai.domain.models

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val publishedAt: Long,
    val durationSeconds: Long,
    val viewCount: Long,
    val type: VideoType,
    val liveStatus: LiveStatus?,
    val scheduledStartTime: Long?
)

enum class VideoType { SHORT, REGULAR, LIVE }

enum class LiveStatus { UPCOMING, ACTIVE, COMPLETED }