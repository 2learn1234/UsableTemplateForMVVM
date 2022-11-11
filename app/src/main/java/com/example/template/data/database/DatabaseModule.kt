package com.example.template.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val TAG = "DataBaseModule"

    @Provides
    fun provideEventDao(database: EventDatabase): EventDao {
        Log.d(TAG, "provideEventDao: the EventDao object is returned")
        return database.eventDao()
    }

    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context): EventDatabase {
        Log.d(TAG, "provideEventDatabase: the database object is created")
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "Event_database"
        ).fallbackToDestructiveMigration().build()
    }
}