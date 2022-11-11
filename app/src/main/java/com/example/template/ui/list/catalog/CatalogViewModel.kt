package com.example.template.ui.list.catalog

import androidx.lifecycle.*
import com.example.template.data.database.EventEntity
import com.example.template.data.repository.EventRepository
import com.example.template.domain.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: EventRepository
):ViewModel(){

    var liveEventList: LiveData<List<Event>> = repository.getAllEventsFlow().asLiveData()

    enum class Status { SUCCESS, LOADING, ERROR }

     val _liveStatus = MutableLiveData(Status.SUCCESS)
    var liveStatus: LiveData<Status> = _liveStatus

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _liveStatus.postValue(Status.LOADING)
            //delay(1500) // fake delay
            try {
                repository.refresh()
                _liveStatus.postValue(Status.SUCCESS)
            } catch (error: IOException) {
                _liveStatus.postValue(Status.ERROR)
            }
        }
    }

    fun refresh(listData:List<Event>) {
        viewModelScope.launch(Dispatchers.IO) {
            //liveEventList.value=listData
            _liveStatus.postValue(Status.LOADING)
            //delay(1500) // fake delay
            try {
                repository.refresh(listData)
                _liveStatus.postValue(Status.SUCCESS)
            } catch (error: IOException) {
                _liveStatus.postValue(Status.ERROR)
            }
        }
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Event>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

    fun clearLocalData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearLocalData()
            _liveStatus.postValue(Status.SUCCESS)
        }
    }

    fun clearError() {
        _liveStatus.value = Status.SUCCESS
    }
}