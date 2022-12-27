package com.example.poznanhelper.screens.shops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.antique.Antique
import com.example.poznanhelper.model.antique.AntiqueEntity
import com.example.poznanhelper.model.shop.Shop
import com.example.poznanhelper.model.shop.ShopEntity
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
class ShopViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<Shop>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getShops(): Resource<List<Shop>> {
        val list = repository.getShops()
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
            repository.getFavShops()
                .distinctUntilChanged()
                .collect { shops ->
                    val shopsList : MutableList<Shop> = mutableListOf()
                    shops.forEach { shopEntity ->
                        shopsList.add(shopEntity.toShop())
                    }
                    _favouriteList.value = shopsList
                }
        }
    }

    fun addShop(shopEntity: ShopEntity) = viewModelScope.launch { repository.addFavShop(shopEntity)}
    fun removeShop(shopEntity: ShopEntity) = viewModelScope.launch { repository.deleteFavShop(shopEntity)}


}