package com.ahmedkhaled.lovindubai.presentation.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ahmedkhaled.lovindubai.R
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import com.ahmedkhaled.lovindubai.presentation.components.ChannelHeader
import com.ahmedkhaled.lovindubai.presentation.components.ErrorFooter
import com.ahmedkhaled.lovindubai.presentation.components.FullScreenLoading
import com.ahmedkhaled.lovindubai.presentation.components.FullScreenMessage
import com.ahmedkhaled.lovindubai.presentation.components.LoadingFooter
import com.ahmedkhaled.lovindubai.presentation.components.ShortCard
import com.ahmedkhaled.lovindubai.presentation.components.VideoCard
import com.ahmedkhaled.lovindubai.presentation.components.toUserMessage
import com.ahmedkhaled.lovindubai.presentation.viewmodels.HomeViewModel
import kotlin.collections.forEachIndexed
import kotlin.collections.getValue
import kotlin.let

private enum class HomeTab(val type: VideoType, @StringRes val titleRes: Int) {
    SHORTS(VideoType.SHORT, R.string.tab_shorts),
    VIDEOS(VideoType.REGULAR, R.string.tab_videos),
    LIVE(VideoType.LIVE, R.string.tab_live)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onVideoClick: (Video) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val channel by viewModel.channel.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val tabs = HomeTab.entries

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            channel?.let { ChannelHeader(it) }

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == selectedTab,
                        onClick = { selectedTab = index },
                        text = { Text(stringResource(tab.titleRes)) }
                    )
                }
            }

            val type = tabs[selectedTab].type
            key(type) {
                val videos = viewModel.videos.getValue(type).collectAsLazyPagingItems()
                VideoTabContent(
                    type = type,
                    videos = videos,
                    onVideoClick = onVideoClick,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoTabContent(
    type: VideoType,
    videos: LazyPagingItems<Video>,
    onVideoClick: (Video) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val refreshState = videos.loadState.refresh
    val appendState = videos.loadState.append
    val refreshFailedMessage = stringResource(R.string.error_refresh_failed)

    LaunchedEffect(refreshState) {
        if (refreshState is LoadState.Error && videos.itemCount > 0) {
            snackbarHostState.showSnackbar(refreshFailedMessage)
        }
    }

    PullToRefreshBox(
        isRefreshing = refreshState is LoadState.Loading && videos.itemCount > 0,
        onRefresh = { videos.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            videos.itemCount > 0 -> {
                if (type == VideoType.SHORT) {
                    ShortsGrid(videos, onVideoClick)
                } else {
                    VideoList(videos, onVideoClick)
                }
            }

            refreshState is LoadState.Error -> FullScreenMessage(
                message = refreshState.error.toUserMessage(),
                actionLabel = stringResource(R.string.retry),
                onAction = videos::retry
            )

            appendState is LoadState.Error -> FullScreenMessage(
                message = appendState.error.toUserMessage(),
                actionLabel = stringResource(R.string.retry),
                onAction = videos::retry
            )

            appendState.endOfPaginationReached && refreshState is LoadState.NotLoading ->
                FullScreenMessage(message = stringResource(R.string.empty_videos))

            else -> FullScreenLoading()
        }
    }
}

@Composable
private fun VideoList(
    videos: LazyPagingItems<Video>,
    onVideoClick: (Video) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(count = videos.itemCount, key = videos.itemKey { it.id }) { index ->
            videos[index]?.let { video ->
                VideoCard(video = video, onClick = { onVideoClick(video) })
            }
        }
        when (val append = videos.loadState.append) {
            is LoadState.Loading -> item { LoadingFooter() }
            is LoadState.Error -> item { ErrorFooter(append.error, onRetry = videos::retry) }
            else -> Unit
        }
    }
}

@Composable
private fun ShortsGrid(
    videos: LazyPagingItems<Video>,
    onVideoClick: (Video) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(count = videos.itemCount, key = videos.itemKey { it.id }) { index ->
            videos[index]?.let { video ->
                ShortCard(video = video, onClick = { onVideoClick(video) })
            }
        }
        when (val append = videos.loadState.append) {
            is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) { LoadingFooter() }
            is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                ErrorFooter(append.error, onRetry = videos::retry)
            }

            else -> Unit
        }
    }
}