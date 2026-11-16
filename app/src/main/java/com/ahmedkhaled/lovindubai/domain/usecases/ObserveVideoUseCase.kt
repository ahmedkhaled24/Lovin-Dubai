package com.ahmedkhaled.lovindubai.domain.usecases

import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.repos.YouTubeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVideoUseCase @Inject constructor(
    private val repository: YouTubeRepository
) {
    operator fun invoke(videoId: String): Flow<Video?> = repository.observeVideo(videoId)
}