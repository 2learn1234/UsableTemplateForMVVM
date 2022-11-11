package com.example.template.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    var id:String,
    var name: String,
    var type: String,

    @ColumnInfo(name="image_url")
    var image_url: String,

    ) : Parcelable

