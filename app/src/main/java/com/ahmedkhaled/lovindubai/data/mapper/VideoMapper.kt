package com.ahmedkhaled.lovindubai.data.mapper

import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity
import com.ahmedkhaled.lovindubai.data.remote.dto.VideoDto
import com.ahmedkhaled.lovindubai.domain.models.Video

fun VideoDto.toEntity(): VideoEntity? {
    val videoId = id ?: return null
    val publishedAt = parseIsoDateToMillis(snippet?.publishedAt) ?: 0L
    val durationSeconds = parseIsoDurationToSeconds(contentDetails?.duration)
    val classification = VideoClassifier.classify(
        liveBroadcastContent = snippet?.liveBroadcastContent,
        hasLiveStreamingDetails = liveStreamingDetails != null,
        durationSeconds = durationSeconds,
        publishedAtMillis = publishedAt
    )

    return VideoEntity(
        id = videoId,
        title = snippet?.title.orEmpty(),
        description = snippet?.description.orEmpty(),
        thumbnailUrl = snippet?.thumbnails.bestUrl(),
        publishedAt = publishedAt,
        durationSeconds = durationSeconds,
        viewCount = statistics?.viewCount?.toLongOrNull() ?: 0L,
        type = classification.type,
        liveStatus = classification.liveStatus,
        scheduledStartTime = parseIsoDateToMillis(liveStreamingDetails?.scheduledStartTime)
    )
}

fun VideoEntity.toDomain(): Video = Video(
    id = id,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    publishedAt = publishedAt,
    durationSeconds = durationSeconds,
    viewCount = viewCount,
    type = type,
    liveStatus = liveStatus,
    scheduledStartTime = scheduledStartTime
)