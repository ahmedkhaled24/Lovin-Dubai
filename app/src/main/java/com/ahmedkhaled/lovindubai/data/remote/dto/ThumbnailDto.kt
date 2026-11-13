package com.ahmedkhaled.lovindubai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ThumbnailsDto(
    @SerializedName("default") val defaultThumbnail: ThumbnailDto?,
    val medium: ThumbnailDto?,
    val high: ThumbnailDto?,
    val standard: ThumbnailDto?,
    val maxres: ThumbnailDto?
)

data class ThumbnailDto(
    val url: String?,
    val width: Int?,
    val height: Int?
)