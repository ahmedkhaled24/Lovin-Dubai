package com.ahmedkhaled.lovindubai.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ahmedkhaled.lovindubai.data.local.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Upsert
    suspend fun upsert(channel: ChannelEntity)

    @Query("SELECT * FROM channel LIMIT 1")
    fun observeChannel(): Flow<ChannelEntity?>

    @Query("SELECT * FROM channel LIMIT 1")
    suspend fun getChannel(): ChannelEntity?
}