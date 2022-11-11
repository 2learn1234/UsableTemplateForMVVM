package com.example.template.data.webdata

import com.example.template.data.webdata.json.CatalogJson
import com.example.template.data.webdata.json.EmbeddedEvents
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebDataApi {
    @GET("events.json")
    suspend fun getCatalog(@Query("apikey") apikey:String): Response<EmbeddedEvents>
}
