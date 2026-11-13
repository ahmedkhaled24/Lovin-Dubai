package com.ahmedkhaled.lovindubai.data.remote.dto

data class ChannelResponseDto(
    val items: List<ChannelDto>?
)

data class ChannelDto(
    val id: String?,
    val snippet: ChannelSnippetDto?,
    val statistics: ChannelStatisticsDto?,
    val contentDetails: ChannelContentDetailsDto?
)

data class ChannelSnippetDto(
    val title: String?,
    val description: String?,
    val customUrl: String?,
    val thumbnails: ThumbnailsDto?
)

data class ChannelStatisticsDto(
    val viewCount: String?,
    val subscriberCount: String?,
    val videoCount: String?,
    val hiddenSubscriberCount: Boolean?
)

data class ChannelContentDetailsDto(
    val relatedPlaylists: RelatedPlaylistsDto?
)

data class RelatedPlaylistsDto(
    val uploads: String?
)