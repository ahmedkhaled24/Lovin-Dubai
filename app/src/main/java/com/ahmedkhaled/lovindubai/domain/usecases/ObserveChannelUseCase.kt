package com.ahmedkhaled.lovindubai.domain.usecases

import com.ahmedkhaled.lovindubai.domain.models.Channel
import com.ahmedkhaled.lovindubai.domain.repos.YouTubeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChannelUseCase @Inject constructor(
    private val repository: YouTubeRepository
) {
    operator fun invoke(): Flow<Channel?> = repository.observeChannel()
}