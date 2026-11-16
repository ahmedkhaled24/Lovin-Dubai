package com.ahmedkhaled.lovindubai.domain.usecases

import androidx.paging.PagingData
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import com.ahmedkhaled.lovindubai.domain.repos.YouTubeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: YouTubeRepository
) {
    operator fun invoke(type: VideoType): Flow<PagingData<Video>> = repository.getVideos(type)
}