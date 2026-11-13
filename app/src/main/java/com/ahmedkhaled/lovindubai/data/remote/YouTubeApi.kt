package com.ahmedkhaled.lovindubai.data.remote

import com.ahmedkhaled.lovindubai.data.remote.dto.ChannelResponseDto
import com.ahmedkhaled.lovindubai.data.remote.dto.PlaylistItemsResponseDto
import com.ahmedkhaled.lovindubai.data.remote.dto.VideosResponseDto
import com.ahmedkhaled.lovindubai.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    @GET("channels")
    suspend fun getChannel(
        @Query("forHandle") handle: String = Constants.CHANNEL_HANDLE,
        @Query("part") part: String = "snippet,statistics,contentDetails"
    ): ChannelResponseDto

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("playlistId") playlistId: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = Constants.PAGE_SIZE,
        @Query("part") part: String = "contentDetails"
    ): PlaylistItemsResponseDto

    @GET("videos")
    suspend fun getVideos(
        @Query("id") ids: String,
        @Query("part") part: String = "snippet,contentDetails,statistics,liveStreamingDetails"
    ): VideosResponseDto
}