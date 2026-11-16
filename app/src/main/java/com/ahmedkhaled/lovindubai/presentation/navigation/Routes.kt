package com.ahmedkhaled.lovindubai.presentation.navigation

object Routes {

    const val HOME = "home"

    const val VIDEO_ID_ARG = "videoId"

    const val VIDEO_DETAILS = "video/{$VIDEO_ID_ARG}"

    fun videoDetails(videoId: String) = "video/$videoId"
}