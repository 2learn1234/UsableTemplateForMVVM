package com.example.template.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.example.template.data.database.EventDao
import com.example.template.data.database.EventEntity
import com.example.template.data.webdata.UrlData
import com.example.template.data.webdata.WebDataApi
import com.example.template.data.webdata.json.CatalogJson
import com.example.template.data.webdata.json.EventJson
import com.example.template.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryWebRoom @Inject constructor(
    private val eventDao: EventDao,
    private val webDataApi: WebDataApi
) : EventRepository {
    override fun getAllEventsFlow(): Flow<List<Event>> =
        eventDao.getAllEventEntitiesFlow()
            .map { entityList ->
                entityList.map { entity ->
                    entity.asEvent()
                }
            }.flowOn(Dispatchers.IO)


    override fun getEventByIdFlow(id: String): Flow<Event> =
        eventDao.getEventEntityByIdFlow(id)
            .map { eventEntity ->
                eventEntity.asEvent()
            }.flowOn(Dispatchers.IO)

    override fun searchDatabase(query: String): Flow<List<Event>> =
        eventDao.searchName(query).map { entityList ->
            entityList.map { entity ->
                entity.asEvent()
            }
        }.flowOn(Dispatchers.IO)



    @Synchronized
    override suspend fun refresh() {
        val eventsResponse = webDataApi?.getCatalog(UrlData.APIKEY)
        //    Log.e("ticketMaster- Embedded", eventsResponse.body().toString())
        var catalog: CatalogJson?

        try {
            eventsResponse?.body().let {
                    catalog = eventsResponse?.body()?._embedded
            }
        } catch (e: Exception) {
            return
        }


        Log.e("ticketMaster-Catalog2", catalog?.events.toString())

        //val eventsResponse =eventsService.getEvents("DW0E98NrxUIfDDtNN7ijruVSm60ryFLX")
        //catalog.events.toString()
        //val embeddedEventsResponse: Response<EmbeddedEvents> = webDataApi.getCatalog(UrlData.APIKEY)
        //  Log.e("TAG",embeddedEventsResponse.body().toString())
        //   val catalog:CatalogJson=embeddedEventsResponse.body()!!._embedded
        //    Log.e("TAG",catalog.toString())
        var entityList: List<EventEntity>?=null
        try {
            entityList=
                catalog!!.events.map { eventJson ->
                    Log.e("TAG -eventJson", eventJson.toString())
                    eventJson.asEntity()


                }
        } catch (e: Exception) {
        }
        try {
            eventDao.refresh(entityList)
        } catch (e: Exception) {
        }
    }

    override suspend fun refresh(liveData: List<Event>) {
        liveData.asFlow().flowOn(Dispatchers.IO)
    }




    override suspend fun clearLocalData() {
        TODO("Not yet implemented")
    }
}


private fun EventEntity.asEvent() =
    Event(
        id = id,
        name = name,
        type = type,
        image_url = image_url
    )


fun EventJson.asEntity() =
    EventEntity(
        id = id,
        name = name,
        type = type,
        image_url = if (images[0] == null) UrlData.placeholderurl else images[0].url
    )
