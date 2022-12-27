package com.example.poznanhelper.screens.antique

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.antique.Antique
import com.example.poznanhelper.model.antique.AntiqueEntity
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
class AntiqueViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<Antique>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getAntiques(): Resource<List<Antique>> {
        val list = repository.getAntiques()
        list.data!!.forEach {
            val lat = it.geometry.coordinates[1].toString().toDouble()
            val lon = it.geometry.coordinates[0].toString().toDouble()

            it.distanceTo = getDistanceInKm(lat, lon)

        }
        list.data = list.data!!.sortedBy {
            it.distanceTo
        }
        return list
    }

    private fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavAntiques()
                .distinctUntilChanged()
                .collect { antiques ->
                    val antiquesList : MutableList<Antique> = mutableListOf()
                    antiques.forEach { antiqueEntity ->
                        antiquesList.add(antiqueEntity.toAntique())
                    }
                    _favouriteList.value = antiquesList
                }
        }
    }

    fun addAntique(antiqueEntity: AntiqueEntity) = viewModelScope.launch { repository.addFavAntique(antiqueEntity)}
    fun removeAntique(antiqueEntity: AntiqueEntity) = viewModelScope.launch { repository.deleteFavAntique(antiqueEntity)}
}