package com.ahmedkhaled.lovindubai.data.mapper

import com.ahmedkhaled.lovindubai.data.local.entity.ChannelEntity
import com.ahmedkhaled.lovindubai.data.remote.dto.ChannelDto
import com.ahmedkhaled.lovindubai.domain.models.Channel

fun ChannelDto.toEntity(lastUpdated: Long): ChannelEntity? {
    val channelId = id ?: return null
    val uploadsPlaylistId = contentDetails?.relatedPlaylists?.uploads ?: return null

    return ChannelEntity(
        id = channelId,
        title = snippet?.title.orEmpty(),
        description = snippet?.description.orEmpty(),
        thumbnailUrl = snippet?.thumbnails.bestUrl(),
        subscriberCount = statistics?.subscriberCount?.toLongOrNull() ?: 0L,
        videoCount = statistics?.videoCount?.toLongOrNull() ?: 0L,
        viewCount = statistics?.viewCount?.toLongOrNull() ?: 0L,
        uploadsPlaylistId = uploadsPlaylistId,
        lastUpdated = lastUpdated
    )
}

fun ChannelEntity.toDomain(): Channel = Channel(
    id = id,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    subscriberCount = subscriberCount,
    videoCount = videoCount,
    viewCount = viewCount,
    uploadsPlaylistId = uploadsPlaylistId
)