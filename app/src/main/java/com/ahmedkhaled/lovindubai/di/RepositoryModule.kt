package com.ahmedkhaled.lovindubai.di

import com.ahmedkhaled.lovindubai.data.repoImpl.YouTubeRepositoryImpl
import com.ahmedkhaled.lovindubai.domain.repos.YouTubeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindYouTubeRepository(impl: YouTubeRepositoryImpl): YouTubeRepository
}