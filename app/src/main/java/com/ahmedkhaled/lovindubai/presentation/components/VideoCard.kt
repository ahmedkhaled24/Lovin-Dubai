package com.ahmedkhaled.lovindubai.presentation.components

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ahmedkhaled.lovindubai.R
import com.ahmedkhaled.lovindubai.domain.models.LiveStatus
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.utils.formatCount
import com.ahmedkhaled.lovindubai.utils.formatDuration

@Composable
fun VideoCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            VideoBadge(
                video = video,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = videoMetaText(video),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun VideoBadge(video: Video, modifier: Modifier = Modifier) {
    val overlay = Color.Black.copy(alpha = 0.8f)
    val (text, color) = when (video.liveStatus) {
        LiveStatus.ACTIVE -> stringResource(R.string.badge_live) to MaterialTheme.colorScheme.error
        LiveStatus.UPCOMING -> stringResource(R.string.badge_upcoming) to overlay
        else -> {
            if (video.durationSeconds <= 0) return
            formatDuration(video.durationSeconds) to overlay
        }
    }
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(color, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun videoMetaText(video: Video): String {
    val context = LocalContext.current
    return when (video.liveStatus) {
        LiveStatus.ACTIVE -> stringResource(R.string.live_now)
        LiveStatus.UPCOMING -> {
            val startTime = video.scheduledStartTime ?: video.publishedAt
            val formatted = DateUtils.formatDateTime(
                context,
                startTime,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME
            )
            stringResource(R.string.scheduled_for, formatted)
        }
        else -> stringResource(
            R.string.video_meta,
            formatCount(video.viewCount),
            DateUtils.getRelativeTimeSpanString(video.publishedAt).toString()
        )
    }
}