package com.example.poznanhelper.screens.bikes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.bike.BikeStation
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.repository.DataRepository
import com.example.poznanhelper.utils.getDistanceInKm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikesViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<BikeStation>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getBikeStations(): Resource<List<BikeStation>> {
        val list = repository.getBikesStation()
        list.data!!.forEach {
            it.distanceTo = getDistanceInKm(it.geometry.coordinates[1], it.geometry.coordinates[0])
        }
        list.data = list.data!!.sortedBy {
            it.distanceTo
        }
        return list
    }

    private fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavBikes()
                .distinctUntilChanged()
                .collect { bikes ->
                    val bikesList : MutableList<BikeStation> = mutableListOf()
                    bikes.forEach { bikeEntity ->
                        bikesList.add(bikeEntity.toBikeStation())
                    }
                    _favouriteList.value = bikesList
                }
        }
    }

    fun addBikeStation(bikeStationEntity: BikeStationEntity) = viewModelScope.launch { repository.addFavBike(bikeStationEntity)}
    fun removeBikeStation(bikeStationEntity: BikeStationEntity) = viewModelScope.launch { repository.deleteFavBike(bikeStationEntity)}
}