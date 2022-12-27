package com.example.poznanhelper.network

import com.example.poznanhelper.model.antique.AntiqueResult
import com.example.poznanhelper.model.bike.BikesResult
import com.example.poznanhelper.model.parking.ResultParking
import com.example.poznanhelper.model.shop.ShopResult
import com.example.poznanhelper.model.ticket.TicketResult
import com.example.poznanhelper.model.ztm.ZtmResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkApi {
    @GET("plan/map_service.html")
    suspend fun getParkingMeters(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "parking_meters"
    ): ResultParking

    @GET("plan/map_service.html")
    suspend fun getBikes(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "stacje_rowerowe"
    ): BikesResult

    @GET("plan/map_service.html")
    suspend fun getTickets(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "class_objects",
        @Query("class_id")classId: String = "4000"
    ): TicketResult

    @GET("plan/map_service.html")
    suspend fun getZtmStations(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "cluster"
    ): ZtmResult

    @GET("plan/map_service.html")
    suspend fun getAntiques(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "class_objects",
        @Query("class_id")classId: String = "2572"
    ): AntiqueResult

    @GET("plan/map_service.html")
    suspend fun getShops(
        @Query("mtype")mType: String = "pub_transport",
        @Query("co")category: String = "class_objects",
        @Query("class_id")classId: String = "4803"
    ): ShopResult


}