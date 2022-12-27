package com.example.poznanhelper.screens.ztm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.bike.BikeStation
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.model.ztm.ZtmEntity
import com.example.poznanhelper.model.ztm.ZtmStation
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
class ZtmViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<ZtmStation>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getZtmStations(): Resource<List<ZtmStation>> {
        val list = repository.getZtmStations()
        list.data!!.forEach {
            it.distanceTo = getDistanceInKm(it.geometry.coordinates[1], it.geometry.coordinates[0])
        }
        list.data = list.data!!.sortedBy {
            it.distanceTo
        }
        list.data = list.data!!.groupBy({ it.id }, { it })
            .mapValues { it.value.reduce{ left, right ->
                left.properties.headsigns += ", ${right.properties.headsigns}"
                ZtmStation(
                    geometry = left.geometry,
                    id = left.id,
                    properties = left.properties,
                    type = left.type,
                    distanceTo = left.distanceTo
                ) } }
            .values.toList()
        return list
    }

    private fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavZtm()
                .distinctUntilChanged()
                .collect { ztmStations ->
                    val ztmList : MutableList<ZtmStation> = mutableListOf()
                    ztmStations.forEach { ztm ->
                        ztmList.add(ztm.toZtmStation())
                    }
                    _favouriteList.value = ztmList
                }
        }
    }

    fun addZtmStation(ztmEntity: ZtmEntity) = viewModelScope.launch { repository.addFavZtm(ztmEntity)}
    fun removeZtmStation(ztmEntity: ZtmEntity) = viewModelScope.launch { repository.deleteFavZtm(ztmEntity)}
}