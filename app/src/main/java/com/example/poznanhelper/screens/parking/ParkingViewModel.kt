package com.example.poznanhelper.screens.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.parking.ParkingMeter
import com.example.poznanhelper.model.parking.ParkingMeterEntity
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
class ParkingViewModel @Inject constructor(private val dataRepository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<ParkingMeter>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getParkingMeters(): Resource<List<ParkingMeter>> {
        val list = dataRepository.getParkingMeters()
        list.data!!.forEach {
            it.distanceTo = getDistanceInKm(it.geometry.coordinates[1], it.geometry.coordinates[0])
        }
        list.data = list.data!!.sortedBy {
            it.distanceTo
        }
        return list
    }

    fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.getFavouriteParkings()
                .distinctUntilChanged()
                .collect { parkings ->
                    val parkingMetersList : MutableList<ParkingMeter> = mutableListOf()
                    parkings.forEach { parkingEntity ->
                        parkingMetersList.add(parkingEntity.toParkingMeter())
                    }
                    _favouriteList.value = parkingMetersList
                }
        }
    }

    fun addParking(parkingMeterEntity: ParkingMeterEntity) = viewModelScope.launch { dataRepository.addFavouriteParking(parkingMeterEntity)}
    fun removeParking(parkingMeterEntity: ParkingMeterEntity) = viewModelScope.launch { dataRepository.deleteFavouriteParking(parkingMeterEntity)}
}