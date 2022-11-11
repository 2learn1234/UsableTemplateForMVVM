package com.example.template.data.webdata.json

data class EventJson(
    val id: String,
    val name: String,
    val type: String,
    val images: List<ImageJson>
)