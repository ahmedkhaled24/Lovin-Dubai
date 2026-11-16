package com.ahmedkhaled.lovindubai.presentation.screens.videodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedkhaled.lovindubai.domain.usecases.ObserveVideoUseCase
import com.ahmedkhaled.lovindubai.presentation.navigation.Routes
import com.ahmedkhaled.lovindubai.presentation.state.VideoDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeVideo: ObserveVideoUseCase
) : ViewModel() {

    private val videoId: String = checkNotNull(savedStateHandle[Routes.VIDEO_ID_ARG])

    val uiState: StateFlow<VideoDetailsUiState> = observeVideo(videoId)
        .map { video ->
            if (video != null) VideoDetailsUiState.Success(video) else VideoDetailsUiState.NotFound
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VideoDetailsUiState.Loading)
}