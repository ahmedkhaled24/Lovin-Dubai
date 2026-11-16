package com.ahmedkhaled.lovindubai.data.local

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmedkhaled.lovindubai.data.local.dao.VideoDao
import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity
import com.ahmedkhaled.lovindubai.domain.models.LiveStatus
import com.ahmedkhaled.lovindubai.domain.models.VideoType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VideoTabsQueryTest {

    private lateinit var database: LovinDubaiDatabase
    private lateinit var videoDao: VideoDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LovinDubaiDatabase::class.java
        ).build()
        videoDao = database.videoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun video(
        id: String,
        type: VideoType,
        publishedAt: Long = 0L,
        liveStatus: LiveStatus? = null
    ) = VideoEntity(
        id = id,
        title = "Video $id",
        description = "",
        thumbnailUrl = null,
        publishedAt = publishedAt,
        durationSeconds = 600L,
        viewCount = 0L,
        type = type,
        liveStatus = liveStatus,
        scheduledStartTime = null
    )

    private suspend fun loadTab(type: VideoType): List<String> {
        val result = videoDao.pagingSource(type).load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 50, placeholdersEnabled = false)
        )
        return (result as PagingSource.LoadResult.Page).data.map { it.id }
    }

    @Test
    fun eachTabReturnsOnlyItsOwnType() = runBlocking<Unit> {
        videoDao.upsertAll(
            listOf(
                video("short1", VideoType.SHORT),
                video("regular1", VideoType.REGULAR),
                video("live1", VideoType.LIVE, liveStatus = LiveStatus.COMPLETED)
            )
        )

        assertEquals(listOf("short1"), loadTab(VideoType.SHORT))
        assertEquals(listOf("regular1"), loadTab(VideoType.REGULAR))
        assertEquals(listOf("live1"), loadTab(VideoType.LIVE))
    }

    @Test
    fun videosAreSortedNewestFirst() = runBlocking<Unit> {
        videoDao.upsertAll(
            listOf(
                video("old", VideoType.REGULAR, publishedAt = 1_000L),
                video("newest", VideoType.REGULAR, publishedAt = 3_000L),
                video("middle", VideoType.REGULAR, publishedAt = 2_000L)
            )
        )

        assertEquals(listOf("newest", "middle", "old"), loadTab(VideoType.REGULAR))
    }
}