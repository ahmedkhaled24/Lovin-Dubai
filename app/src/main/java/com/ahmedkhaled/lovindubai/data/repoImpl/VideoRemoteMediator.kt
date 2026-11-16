package com.ahmedkhaled.lovindubai.data.repoImpl

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ahmedkhaled.lovindubai.data.local.LovinDubaiDatabase
import com.ahmedkhaled.lovindubai.data.local.entity.RemoteKeyEntity
import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity
import com.ahmedkhaled.lovindubai.data.mapper.toEntity
import com.ahmedkhaled.lovindubai.data.remote.YouTubeApi
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
class VideoRemoteMediator(
    private val api: YouTubeApi,
    private val database: LovinDubaiDatabase,
    private val clock: () -> Long = { System.currentTimeMillis() }
) : RemoteMediator<Int, VideoEntity>() {

    private val videoDao = database.videoDao()
    private val channelDao = database.channelDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeyDao.getKey(UPLOADS_KEY)?.lastUpdated
            ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        return if (clock() - lastUpdated < CACHE_TIMEOUT_MILLIS) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, VideoEntity>
    ): MediatorResult {
        val savedKey = remoteKeyDao.getKey(UPLOADS_KEY)

        val pageToken = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                if (savedKey != null && savedKey.nextPageToken == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                savedKey?.nextPageToken
            }
        }

        return try {
            val playlistId = getUploadsPlaylistId(forceRefresh = loadType == LoadType.REFRESH)
            val page = api.getPlaylistItems(playlistId = playlistId, pageToken = pageToken)
            val videoIds = page.items.orEmpty().mapNotNull { it.contentDetails?.videoId }

            val videos = if (videoIds.isEmpty()) {
                emptyList()
            } else {
                api.getVideos(ids = videoIds.joinToString(",")).items.orEmpty()
                    .mapNotNull { it.toEntity() }
            }

            val lastUpdated =
                if (loadType == LoadType.REFRESH) clock() else savedKey?.lastUpdated ?: clock()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    videoDao.clearAll()
                }
                remoteKeyDao.upsert(RemoteKeyEntity(UPLOADS_KEY, page.nextPageToken, lastUpdated))
                videoDao.upsertAll(videos)
            }

            MediatorResult.Success(endOfPaginationReached = page.nextPageToken == null)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getUploadsPlaylistId(forceRefresh: Boolean): String {
        if (!forceRefresh) {
            channelDao.getChannel()?.let { return it.uploadsPlaylistId }
        }

        val channel = api.getChannel().items?.firstOrNull()?.toEntity(lastUpdated = clock())
            ?: throw IllegalStateException("Channel not found")

        channelDao.upsert(channel)
        return channel.uploadsPlaylistId
    }

    companion object {
        const val UPLOADS_KEY = "uploads"
        private val CACHE_TIMEOUT_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}