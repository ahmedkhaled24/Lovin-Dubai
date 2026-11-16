package com.ahmedkhaled.lovindubai.presentation.state

import com.ahmedkhaled.lovindubai.domain.models.Video

sealed interface VideoDetailsUiState {
    data object Loading : VideoDetailsUiState
    data class Success(val video: Video) : VideoDetailsUiState
    data object NotFound : VideoDetailsUiState
}