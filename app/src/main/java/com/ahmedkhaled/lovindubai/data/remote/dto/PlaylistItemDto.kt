package com.ahmedkhaled.lovindubai.data.remote.dto

data class PlaylistItemsResponseDto(
    val nextPageToken: String?,
    val items: List<PlaylistItemDto>?
)

data class PlaylistItemDto(
    val contentDetails: PlaylistItemContentDetailsDto?
)

data class PlaylistItemContentDetailsDto(
    val videoId: String?,
    val videoPublishedAt: String?
)