package com.example.poznanhelper.screens.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.bike.BikeStation
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.model.ticket.Ticket
import com.example.poznanhelper.model.ticket.TicketEntity
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
class TicketViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {
    private val _favouriteList = MutableStateFlow<List<Ticket>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getFavourites()
    }

    suspend fun getTickets(): Resource<List<Ticket>> {
        val list = repository.getTickets()
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
            repository.getFavTickets()
                .distinctUntilChanged()
                .collect { tickets ->
                    val ticketsList : MutableList<Ticket> = mutableListOf()
                    tickets.forEach { ticketEntity ->
                        ticketsList.add(ticketEntity.toTicket())
                    }
                    _favouriteList.value = ticketsList
                }
        }
    }

    fun addTicket(ticketEntity: TicketEntity) = viewModelScope.launch { repository.addFavTicket(ticketEntity)}
    fun removeTicket(ticketEntity: TicketEntity) = viewModelScope.launch { repository.deleteFavTicket(ticketEntity)}
}