package com.ahmedkhaled.lovindubai.data.remote.dto

data class VideosResponseDto(
    val items: List<VideoDto>?
)

data class VideoDto(
    val id: String?,
    val snippet: VideoSnippetDto?,
    val contentDetails: VideoContentDetailsDto?,
    val statistics: VideoStatisticsDto?,
    val liveStreamingDetails: LiveStreamingDetailsDto?
)

data class VideoSnippetDto(
    val publishedAt: String?,
    val title: String?,
    val description: String?,
    val thumbnails: ThumbnailsDto?,
    val liveBroadcastContent: String?
)

data class VideoContentDetailsDto(
    val duration: String?
)

data class VideoStatisticsDto(
    val viewCount: String?,
    val likeCount: String?
)

data class LiveStreamingDetailsDto(
    val actualStartTime: String?,
    val actualEndTime: String?,
    val scheduledStartTime: String?,
    val concurrentViewers: String?
)