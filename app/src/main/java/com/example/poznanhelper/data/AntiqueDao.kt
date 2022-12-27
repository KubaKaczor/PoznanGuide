package com.example.poznanhelper.data

import androidx.room.*
import com.example.poznanhelper.model.antique.AntiqueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AntiqueDao {
    @Query("Select * from antiques")
    fun getAntiques(): Flow<List<AntiqueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(antiqueEntity: AntiqueEntity)

    @Delete
    suspend fun delete(antiqueEntity: AntiqueEntity)
}