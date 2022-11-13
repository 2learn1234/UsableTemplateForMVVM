package com.example.template.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.template.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
 class TempViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository:EventRepository
): ViewModel() {

    private val eventId: String = savedStateHandle["eventId"] ?:
    throw IllegalArgumentException("missing event id")

      val tempLiveData=repository.getEventByIdFlow(eventId).asLiveData()
}