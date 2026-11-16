package com.ahmedkhaled.lovindubai.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Upsert
    suspend fun upsertAll(videos: List<VideoEntity>)

    @Query("SELECT * FROM videos WHERE id = :id")
    fun observeVideo(id: String): Flow<VideoEntity?>

    @Query("SELECT * FROM videos WHERE type = :type ORDER BY publishedAt DESC")
    fun pagingSource(type: VideoType): PagingSource<Int, VideoEntity>

    @Query("DELETE FROM videos")
    suspend fun clearAll()
}