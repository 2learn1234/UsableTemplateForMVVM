package com.example.template.data.repository

import androidx.lifecycle.LiveData
import com.example.template.data.database.EventEntity
import com.example.template.data.webdata.json.EventJson
import com.example.template.domain.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
   fun getAllEventsFlow(): Flow<List<Event>>
   fun getEventByIdFlow(id:String):Flow<Event>
   fun searchDatabase (query:String):Flow<List<Event>>
   suspend fun refresh()
   suspend fun refresh(liveData:List<Event>)
   suspend fun clearLocalData()
}