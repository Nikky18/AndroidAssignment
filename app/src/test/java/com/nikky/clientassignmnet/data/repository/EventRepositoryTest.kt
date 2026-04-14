package com.nikky.clientassignmnet.data.repository

import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.remote.EventApi
import com.nikky.clientassignmnet.data.remote.EventDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class EventRepositoryTest {
    private lateinit var repo: EventRepository
    private val api: EventApi = mockk()
    private val dao: EventDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repo = EventRepository(api, dao)
    }

    @Test
    fun `refresh should map api response correctly`() = runBlocking {

        // GIVEN
        val dto = EventDto(
            id = "1",
            title = "Test Event",
            location = "Bhopal",
            time = 123456789,
            imageUrl = "url",
            lat = 10.0,
            lng = 20.0
        )

        coEvery { api.getEvents() } returns listOf(dto)
        coEvery { dao.getEventsOnce() } returns emptyList()

        val slot = slot<List<EventEntity>>()
        coEvery { dao.insertEvents(capture(slot)) }

        repo.refresh()

        val savedList = slot.captured

        assertEquals(1, savedList.size)
        assertEquals("Test Event", savedList[0].title)
        assertEquals("Bhopal", savedList[0].location)
        assertEquals(10.0, savedList[0].lat, 0.0)
    }

    @Test
    fun `toggleBookmark should invert bookmark value`() = runBlocking {
        val event = EventEntity(
            id = "1",
            title = "Test",
            location = "Bhopal",
            time = 123,
            imageUrl = "",
            lat = 0.0,
            lng = 0.0,
            isBookmarked = false
        )

        repo.toggleBookmark(event)

        coVerify {
            dao.updateBookmark("1", true)
        }
    }

}