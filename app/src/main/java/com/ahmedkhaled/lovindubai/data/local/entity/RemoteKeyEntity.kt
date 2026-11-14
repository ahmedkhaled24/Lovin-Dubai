package com.ahmedkhaled.lovindubai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextPageToken: String?,
    val lastUpdated: Long
)