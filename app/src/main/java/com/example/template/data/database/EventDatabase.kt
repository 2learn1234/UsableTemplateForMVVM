package com.example.template.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EventEntity::class), version = 2, exportSchema = false)
abstract class EventDatabase:RoomDatabase() {
    abstract fun eventDao():EventDao
}