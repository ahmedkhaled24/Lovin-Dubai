package com.ahmedkhaled.lovindubai.data.repoImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ahmedkhaled.lovindubai.data.local.LovinDubaiDatabase
import com.ahmedkhaled.lovindubai.data.mapper.toDomain
import com.ahmedkhaled.lovindubai.data.remote.YouTubeApi
import com.ahmedkhaled.lovindubai.domain.models.Channel
import com.ahmedkhaled.lovindubai.domain.models.Video
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import com.ahmedkhaled.lovindubai.domain.repos.YouTubeRepository
import com.ahmedkhaled.lovindubai.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class YouTubeRepositoryImpl @Inject constructor(
    private val api: YouTubeApi,
    private val database: LovinDubaiDatabase
) : YouTubeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getVideos(type: VideoType): Flow<PagingData<Video>> {
        val pager = Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = VideoRemoteMediator(api, database),
            pagingSourceFactory = {
                database.videoDao().pagingSource(type)
            }
        )
        return pager.flow.map { pagingData -> pagingData.map { video -> video.toDomain() } }
    }

    override fun observeChannel(): Flow<Channel?> =
        database.channelDao().observeChannel().map { it?.toDomain() }

    override fun observeVideo(id: String): Flow<Video?> =
        database.videoDao().observeVideo(id).map { it?.toDomain() }
}