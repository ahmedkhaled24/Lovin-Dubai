package com.ahmedkhaled.lovindubai.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType

fun openInYouTube(context: Context, video: Video) {
    val url = if (video.type == VideoType.SHORT) {
        "https://www.youtube.com/shorts/${video.id}"
    } else {
        "https://www.youtube.com/watch?v=${video.id}"
    }
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: ActivityNotFoundException) {
        // No app can open the link
    }
}