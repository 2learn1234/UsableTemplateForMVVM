package com.example.template.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getAllEventEntitiesFlow():Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventEntityByIdFlow(id: String): Flow<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventEntityList(list: List<EventEntity>)

    @Query("Delete FROM events")
    suspend fun deleteAllEventEntities()

    @Query("SELECT * FROM events WHERE name LIKE '%'||:queryString || '%'")
     fun searchName(queryString:String):Flow<List<EventEntity>>

    @Transaction
    suspend fun refresh(list: List<EventEntity>?){
        deleteAllEventEntities()
        insertEventEntityList(list!!)
    }


}