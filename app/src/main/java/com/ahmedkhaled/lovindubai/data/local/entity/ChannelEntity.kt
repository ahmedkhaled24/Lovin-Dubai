package com.ahmedkhaled.lovindubai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel")
data class ChannelEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val subscriberCount: Long,
    val videoCount: Long,
    val viewCount: Long,
    val uploadsPlaylistId: String,
    val lastUpdated: Long
)