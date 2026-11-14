package com.ahmedkhaled.lovindubai.domain.repos

import androidx.paging.PagingData
import com.ahmedkhaled.lovindubai.domain.models.Channel
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import kotlinx.coroutines.flow.Flow

interface YouTubeRepository {
    fun getVideos(type: VideoType): Flow<PagingData<Video>>
    fun observeChannel(): Flow<Channel?>
}