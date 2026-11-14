package com.ahmedkhaled.lovindubai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmedkhaled.lovindubai.data.local.dao.ChannelDao
import com.ahmedkhaled.lovindubai.data.local.dao.RemoteKeyDao
import com.ahmedkhaled.lovindubai.data.local.dao.VideoDao
import com.ahmedkhaled.lovindubai.data.local.entity.ChannelEntity
import com.ahmedkhaled.lovindubai.data.local.entity.RemoteKeyEntity
import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity

@Database(
    entities = [VideoEntity::class, ChannelEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LovinDubaiDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun channelDao(): ChannelDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}