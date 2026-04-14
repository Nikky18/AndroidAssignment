package com.nikky.clientassignmnet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)


    @Query("UPDATE events SET isBookmarked = :value WHERE id = :id")
    suspend fun updateBookmark(id: String, value: Boolean)

    @Query("SELECT * FROM events")
    suspend fun getEventsOnce(): List<EventEntity>

    @Query("SELECT * FROM events WHERE isBookmarked = 1")
    fun getBookmarkedEvents(): Flow<List<EventEntity>>

}