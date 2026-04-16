package com.nikky.clientassignmnet.data.repository

import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.remote.EventApi
import com.nikky.clientassignmnet.data.remote.EventDto
import com.nikky.clientassignmnet.domain.model.Event
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EventRepositoryImplTest {
    private lateinit var repo: EventRepositoryImpl
    private val api: EventApi = mockk()
    private val dao: EventDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repo = EventRepositoryImpl(api, dao)
    }

    @Test
    fun refresh() = runBlocking {

        // GIVEN
        val dto = EventDto(
            id = "1",
            title = "Test Event",
            location = "Bhopal",
            time = 123456,
            imageUrl = "url",
            lat = 10.0,
            lng = 20.0
        )

        coEvery { api.getEvents() } returns listOf(dto)
        coEvery { dao.getEventsOnce() } returns emptyList()

        val slot = slot<List<EventEntity>>()
        coEvery { dao.insertEvents(capture(slot)) } just Runs

        // WHEN
        repo.refresh()

        // THEN
        val saved = slot.captured

        assertEquals(1, saved.size)
        assertEquals("1", saved[0].id)
        assertEquals("Test Event", saved[0].title)
        assertEquals("Bhopal", saved[0].location)
        assertEquals(false, saved[0].isBookmarked)
    }

    @Test
    fun getEvents() = runBlocking {

        val entityList = listOf(
            EventEntity(
                id = "1",
                title = "Test Event",
                location = "Bhopal",
                time = 123,
                imageUrl = "",
                lat = 10.0,
                lng = 20.0,
                isBookmarked = false
            )
        )

        coEvery { dao.getEvents() } returns kotlinx.coroutines.flow.flowOf(entityList)

        val result = repo.getEvents().first()

        assertEquals(1, result.size)
        assertEquals("Test Event", result[0].title)
        assertEquals("Bhopal", result[0].location)
    }

    @Test
    fun getBookmarkedEvents() = runBlocking {

        val entityList = listOf(
            EventEntity(
                id = "1",
                title = "Bookmark Event",
                location = "Delhi",
                time = 123,
                imageUrl = "",
                lat = 0.0,
                lng = 0.0,
                isBookmarked = true
            )
        )

        coEvery { dao.getBookmarkedEvents() } returns kotlinx.coroutines.flow.flowOf(entityList)

        val result = repo.getBookmarkedEvents().first()

        assertEquals(1, result.size)
        assertEquals("Bookmark Event", result[0].title)
        assertEquals(true, result[0].isBookmarked)
    }

    @Test
    fun getEvents_shouldReturnMappedData() = runBlocking {

        val entityList = listOf(
            EventEntity(
                id = "1",
                title = "Test Event",
                location = "Bhopal",
                time = 123,
                imageUrl = "",
                lat = 10.0,
                lng = 20.0,
                isBookmarked = false
            )
        )

        coEvery { dao.getEvents() } returns kotlinx.coroutines.flow.flowOf(entityList)

        val result = repo.getEvents().first()

        assertEquals(1, result.size)
        assertEquals("Test Event", result[0].title)
    }

    @Test
    fun getEvents_shouldReturnEmptyList_whenNoData() = runBlocking {

        coEvery { dao.getEvents() } returns kotlinx.coroutines.flow.flowOf(emptyList())

        val result = repo.getEvents().first()

        assertEquals(0, result.size)
    }

    @Test
    fun getEvents_shouldPreserveBookmarkState() = runBlocking {

        val entityList = listOf(
            EventEntity(
                id = "1",
                title = "Event",
                location = "Delhi",
                time = 123,
                imageUrl = "",
                lat = 0.0,
                lng = 0.0,
                isBookmarked = true
            )
        )

        coEvery { dao.getEvents() } returns kotlinx.coroutines.flow.flowOf(entityList)

        val result = repo.getEvents().first()

        assertEquals(true, result[0].isBookmarked)
    }


    @Test
    fun toggleBookmark() = runBlocking {

        // GIVEN
        val event = Event(
            id = "1",
            title = "Test",
            location = "Bhopal",
            time = 123,
            imageUrl = "",
            lat = 0.0,
            lng = 0.0,
            isBookmarked = false
        )

        // WHEN
        repo.toggleBookmark(event)

        // THEN
        coVerify {
            dao.updateBookmark("1", true)
        }
    }

}