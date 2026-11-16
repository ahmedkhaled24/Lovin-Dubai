package com.ahmedkhaled.lovindubai.data.repoImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmedkhaled.lovindubai.data.local.LovinDubaiDatabase
import com.ahmedkhaled.lovindubai.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class VideoRemoteMediatorTest {

    private lateinit var database: LovinDubaiDatabase
    private lateinit var fakeApi: FakeYouTubeApi
    private lateinit var mediator: VideoRemoteMediator

    private val pagingState = PagingState<Int, VideoEntity>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 50),
        leadingPlaceholderCount = 0
    )

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LovinDubaiDatabase::class.java
        ).build()
        fakeApi = FakeYouTubeApi()
        mediator = VideoRemoteMediator(fakeApi, database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun deletedVideosAreNotSaved() = runBlocking<Unit> {
        fakeApi.playlistVideoIds = listOf("video1", "deleted1")
        fakeApi.addVideo("video1")

        mediator.load(LoadType.REFRESH, pagingState)

        assertNotNull(database.videoDao().observeVideo("video1").first())
        assertNull(database.videoDao().observeVideo("deleted1").first())
    }

    @Test
    fun refreshFailureKeepsCachedVideos() = runBlocking<Unit> {
        fakeApi.playlistVideoIds = listOf("video1")
        fakeApi.addVideo("video1")
        mediator.load(LoadType.REFRESH, pagingState)

        fakeApi.shouldFail = true
        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertNotNull(database.videoDao().observeVideo("video1").first())
    }

    @Test
    fun refreshUsesOnlyThreeApiCalls() = runBlocking<Unit> {
        fakeApi.playlistVideoIds = listOf("video1", "video2")
        fakeApi.addVideo("video1")
        fakeApi.addVideo("video2")

        mediator.load(LoadType.REFRESH, pagingState)

        assertEquals(3, fakeApi.callCount)
    }
}