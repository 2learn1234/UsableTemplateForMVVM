package com.example.template.domain

import androidx.room.ColumnInfo
import com.example.template.data.webdata.json.ImageJson

data class Event(
    var id:String,
    var name: String,
    var type: String,
    var image_url: String,
)
