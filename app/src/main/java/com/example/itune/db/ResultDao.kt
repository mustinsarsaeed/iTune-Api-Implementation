package com.example.itune.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itune.model.Results

@Dao
interface ResultsDao {

    //For Add data in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result: Results) : Long

    //For get data from DB
    @Query("SELECT * FROM results")
    fun getResults() : LiveData<List<Results>>

    //For unFavorite data from DB
    @Delete
    suspend fun deleteResult(result: Results)

    //For get offline Data
    @Query("SELECT COUNT(*) > 0 FROM results WHERE trackId = :trackId")
    suspend fun isMovieInFavorites(trackId: Int): Boolean

}