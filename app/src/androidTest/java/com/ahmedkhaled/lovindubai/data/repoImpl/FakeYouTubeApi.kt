package com.ahmedkhaled.lovindubai.data.repoImpl

import com.ahmedkhaled.lovindubai.data.remote.YouTubeApi
import com.ahmedkhaled.lovindubai.data.remote.dto.ChannelContentDetailsDto
import com.ahmedkhaled.lovindubai.data.remote.dto.ChannelDto
import com.ahmedkhaled.lovindubai.data.remote.dto.ChannelResponseDto
import com.ahmedkhaled.lovindubai.data.remote.dto.PlaylistItemContentDetailsDto
import com.ahmedkhaled.lovindubai.data.remote.dto.PlaylistItemDto
import com.ahmedkhaled.lovindubai.data.remote.dto.PlaylistItemsResponseDto
import com.ahmedkhaled.lovindubai.data.remote.dto.RelatedPlaylistsDto
import com.ahmedkhaled.lovindubai.data.remote.dto.VideoContentDetailsDto
import com.ahmedkhaled.lovindubai.data.remote.dto.VideoDto
import com.ahmedkhaled.lovindubai.data.remote.dto.VideoSnippetDto
import com.ahmedkhaled.lovindubai.data.remote.dto.VideosResponseDto
import java.io.IOException

class FakeYouTubeApi : YouTubeApi {

    var playlistVideoIds: List<String> = emptyList()
    var shouldFail = false
    var callCount = 0

    private val availableVideos = mutableMapOf<String, VideoDto>()

    override suspend fun getChannel(handle: String, part: String): ChannelResponseDto {
        callCount++
        failIfNeeded()
        return ChannelResponseDto(
            items = listOf(
                ChannelDto(
                    id = "UC123",
                    snippet = null,
                    statistics = null,
                    contentDetails = ChannelContentDetailsDto(RelatedPlaylistsDto(uploads = "UU123"))
                )
            )
        )
    }

    override suspend fun getPlaylistItems(
        playlistId: String,
        pageToken: String?,
        maxResults: Int,
        part: String
    ): PlaylistItemsResponseDto {
        callCount++
        failIfNeeded()
        return PlaylistItemsResponseDto(
            nextPageToken = null,
            items = playlistVideoIds.map { id ->
                PlaylistItemDto(PlaylistItemContentDetailsDto(videoId = id, videoPublishedAt = null))
            }
        )
    }

    override suspend fun getVideos(ids: String, part: String): VideosResponseDto {
        callCount++
        failIfNeeded()
        return VideosResponseDto(items = ids.split(",").mapNotNull { availableVideos[it] })
    }

    fun addVideo(id: String) {
        availableVideos[id] = VideoDto(
            id = id,
            snippet = VideoSnippetDto(
                publishedAt = "2026-01-01T10:00:00Z",
                title = "Video $id",
                description = "",
                thumbnails = null,
                liveBroadcastContent = "none"
            ),
            contentDetails = VideoContentDetailsDto(duration = "PT10M"),
            statistics = null,
            liveStreamingDetails = null
        )
    }

    private fun failIfNeeded() {
        if (shouldFail) throw IOException("No internet")
    }
}