package com.ahmedkhaled.lovindubai.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmedkhaled.lovindubai.domain.models.LiveStatus
import com.ahmedkhaled.lovindubai.domain.models.VideoType

@Entity(
    tableName = "videos",
    indices = [Index(value = ["type", "publishedAt"])]
)
data class VideoEntity(
    @PrimaryKey val id: String,
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