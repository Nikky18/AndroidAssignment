package com.nikky.clientassignmnet.di

import android.content.Context
import androidx.room.Room
import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.local.EventDatabase
import com.nikky.clientassignmnet.data.remote.EventApi
import com.nikky.clientassignmnet.data.repository.EventRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.nikky.clientassignmnet.domain.repository.EventRepository
import dagger.Binds


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "event_db"
        ).build()
    }


    @Provides
    fun provideEventDao(db: EventDatabase): EventDao {
        return db.eventDao()
    }


    @Provides
    fun provideEventApi(
        @ApplicationContext context: Context
    ): EventApi {
        return EventApi(context)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        abstract fun bindEventRepository(
            impl: EventRepositoryImpl
        ): EventRepository
    }
}