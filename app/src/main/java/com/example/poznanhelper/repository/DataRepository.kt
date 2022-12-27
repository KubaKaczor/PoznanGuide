package com.example.poznanhelper.repository

import android.util.Log
import com.example.poznanhelper.data.*
import com.example.poznanhelper.model.antique.Antique
import com.example.poznanhelper.model.bike.BikeStation
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.model.antique.AntiqueEntity
import com.example.poznanhelper.model.parking.ParkingMeter
import com.example.poznanhelper.model.parking.ParkingMeterEntity
import com.example.poznanhelper.model.shop.Shop
import com.example.poznanhelper.model.shop.ShopEntity
import com.example.poznanhelper.model.ticket.Ticket
import com.example.poznanhelper.model.ticket.TicketEntity
import com.example.poznanhelper.model.ztm.ZtmEntity
import com.example.poznanhelper.model.ztm.ZtmStation
import com.example.poznanhelper.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DataRepository @Inject constructor(private val api: NetworkApi,
                                         private val parkingDao: ParkingDao,
                                         private val bikeDao: BikeDao,
                                         private val ticketDao: TicketDao,
                                         private val ztmDao: ZtmDao,
                                         private val antiqueDao: AntiqueDao,
                                         private val shopDao: ShopDao) {
   //parking meters
    suspend fun getParkingMeters(): Resource<List<ParkingMeter>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getParkingMeters().features
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    suspend fun addFavouriteParking(parkingMeterEntity: ParkingMeterEntity) = parkingDao.insert(parkingMeterEntity)
    suspend fun deleteFavouriteParking(parkingMeterEntity: ParkingMeterEntity) = parkingDao.delete(parkingMeterEntity)
    fun getFavouriteParkings(): Flow<List<ParkingMeterEntity>> = parkingDao.getParkingMeters().flowOn(Dispatchers.IO).conflate()

    //bikes
    suspend fun getBikesStation(): Resource<List<BikeStation>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getBikes().bikes
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    suspend fun addFavBike(bikeStationEntity: BikeStationEntity) = bikeDao.insert(bikeStationEntity)
    suspend fun deleteFavBike(bikeStationEntity: BikeStationEntity) = bikeDao.delete(bikeStationEntity)
    fun getFavBikes(): Flow<List<BikeStationEntity>> = bikeDao.getBikeStations().flowOn(Dispatchers.IO).conflate()

    //tickets
    suspend fun getTickets(): Resource<List<Ticket>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getTickets().tickets
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    suspend fun addFavTicket(ticketEntity: TicketEntity) = ticketDao.insert(ticketEntity)
    suspend fun deleteFavTicket(ticketEntity: TicketEntity) = ticketDao.delete(ticketEntity)
    fun getFavTickets(): Flow<List<TicketEntity>> = ticketDao.getTickets().flowOn(Dispatchers.IO).conflate()

    //ztm
    suspend fun getZtmStations(): Resource<List<ZtmStation>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getZtmStations().stations
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    suspend fun addFavZtm(ztmEntity: ZtmEntity) = ztmDao.insert(ztmEntity)
    suspend fun deleteFavZtm(ztmEntity: ZtmEntity) = ztmDao.delete(ztmEntity)
    fun getFavZtm(): Flow<List<ZtmEntity>> = ztmDao.getZtmStations().flowOn(Dispatchers.IO).conflate()

    //antiques
    suspend fun getAntiques(): Resource<List<Antique>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAntiques().features
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }


    suspend fun addFavAntique(antiqueEntity: AntiqueEntity) = antiqueDao.insert(antiqueEntity)
    suspend fun deleteFavAntique(antiqueEntity: AntiqueEntity) = antiqueDao.delete(antiqueEntity)
    fun getFavAntiques(): Flow<List<AntiqueEntity>> = antiqueDao.getAntiques().flowOn(Dispatchers.IO).conflate()

    //shops
    suspend fun getShops(): Resource<List<Shop>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getShops().features
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (ex: Exception) {
            Resource.Error(message = ex.message)
        }
    }

    suspend fun addFavShop(shopEntity: ShopEntity) = shopDao.insert(shopEntity)
    suspend fun deleteFavShop(shopEntity: ShopEntity) = shopDao.delete(shopEntity)
    fun getFavShops(): Flow<List<ShopEntity>> = shopDao.getShops().flowOn(Dispatchers.IO).conflate()


}