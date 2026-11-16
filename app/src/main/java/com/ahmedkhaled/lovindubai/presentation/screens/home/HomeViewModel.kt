package com.ahmedkhaled.lovindubai.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import com.ahmedkhaled.lovindubai.domain.usecases.GetVideosUseCase
import com.ahmedkhaled.lovindubai.domain.usecases.ObserveChannelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    getVideos: GetVideosUseCase,
    observeChannel: ObserveChannelUseCase
) : ViewModel() {

    val videos = mutableMapOf<VideoType, Flow<PagingData<Video>>>()

    init {
        VideoType.entries.forEach { type ->
            videos[type] = getVideos(type).cachedIn(viewModelScope)
        }
    }

    val channel = observeChannel()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}