package com.ahmedkhaled.lovindubai.domain.models

data class Channel(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val subscriberCount: Long,
    val videoCount: Long,
    val viewCount: Long,
    val uploadsPlaylistId: String
)