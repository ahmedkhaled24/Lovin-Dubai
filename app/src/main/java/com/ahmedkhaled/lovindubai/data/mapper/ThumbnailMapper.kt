package com.ahmedkhaled.lovindubai.data.mapper

import com.ahmedkhaled.lovindubai.data.remote.dto.ThumbnailsDto

fun ThumbnailsDto?.bestUrl(): String? =
    this?.let { high?.url ?: medium?.url ?: defaultThumbnail?.url }