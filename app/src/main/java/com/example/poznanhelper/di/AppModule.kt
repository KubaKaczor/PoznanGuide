package com.example.poznanhelper.di

import android.content.Context
import androidx.room.Room
import com.example.poznanhelper.data.*
import com.example.poznanhelper.network.NetworkApi
import com.example.poznanhelper.repository.DataRepository
import com.example.poznanhelper.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDataRepository(api: NetworkApi,
                              parkingDao: ParkingDao,
                              bikeDao: BikeDao,
                              ticketDao: TicketDao,
                              ztmDao: ZtmDao,
                              antiqueDao: AntiqueDao,
                              shopDao: ShopDao): DataRepository {
        return DataRepository(api, parkingDao, bikeDao, ticketDao, ztmDao, antiqueDao, shopDao)
    }

    @Singleton
    @Provides
    fun provideApi(): NetworkApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideParkingDao(appDatabase: AppDatabase): ParkingDao{
        return appDatabase.parkingDao()
    }

    @Singleton
    @Provides
    fun provideBikeDao(appDatabase: AppDatabase): BikeDao{
        return appDatabase.bikeDao()
    }

    @Singleton
    @Provides
    fun provideTicketDao(appDatabase: AppDatabase): TicketDao{
        return appDatabase.ticketDao()
    }

    @Singleton
    @Provides
    fun provideZtmDao(appDatabase: AppDatabase): ZtmDao{
        return appDatabase.ztmDao()
    }

    @Singleton
    @Provides
    fun provideChurchDao(appDatabase: AppDatabase): AntiqueDao{
        return appDatabase.antiqueDao()
    }

    @Singleton
    @Provides
    fun provideShopDao(appDatabase: AppDatabase): ShopDao{
        return appDatabase.shopDao()
    }
}