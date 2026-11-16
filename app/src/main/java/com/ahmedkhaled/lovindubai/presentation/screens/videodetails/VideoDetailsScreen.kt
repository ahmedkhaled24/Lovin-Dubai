package com.ahmedkhaled.lovindubai.presentation.screens.videodetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ahmedkhaled.lovindubai.R
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.presentation.components.FullScreenLoading
import com.ahmedkhaled.lovindubai.presentation.components.FullScreenMessage
import com.ahmedkhaled.lovindubai.presentation.components.videoMetaText
import com.ahmedkhaled.lovindubai.presentation.state.VideoDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailsScreen(
    onBack: () -> Unit,
    onPlay: (Video) -> Unit,
    viewModel: VideoDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val current = state) {
                VideoDetailsUiState.Loading -> FullScreenLoading()
                VideoDetailsUiState.NotFound -> FullScreenMessage(stringResource(R.string.video_unavailable))
                is VideoDetailsUiState.Success -> VideoDetailsContent(
                    video = current.video,
                    onPlay = { onPlay(current.video) }
                )
            }
        }
    }
}

@Composable
private fun VideoDetailsContent(video: Video, onPlay: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clickable(onClick = onPlay),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(R.string.play),
                tint = Color.White,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    .padding(8.dp)
            )
        }

        Column(Modifier.padding(16.dp)) {
            Text(text = video.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                text = videoMetaText(video),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onPlay, modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.watch_on_youtube))
            }
            if (video.description.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(text = video.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}