package com.ahmedkhaled.lovindubai.di

import android.content.Context
import androidx.room.Room
import com.ahmedkhaled.lovindubai.data.local.LovinDubaiDatabase
import com.ahmedkhaled.lovindubai.data.local.dao.ChannelDao
import com.ahmedkhaled.lovindubai.data.local.dao.RemoteKeyDao
import com.ahmedkhaled.lovindubai.data.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LovinDubaiDatabase =
        Room.databaseBuilder(context, LovinDubaiDatabase::class.java, "lovin_dubai.db").build()

    @Provides
    fun provideVideoDao(database: LovinDubaiDatabase): VideoDao = database.videoDao()

    @Provides
    fun provideChannelDao(database: LovinDubaiDatabase): ChannelDao = database.channelDao()

    @Provides
    fun provideRemoteKeyDao(database: LovinDubaiDatabase): RemoteKeyDao = database.remoteKeyDao()
}