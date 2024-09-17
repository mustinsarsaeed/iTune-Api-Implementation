package com.example.itune.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itune.Results

@Dao
interface ResultsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result: Results) : Long

    @Query("SELECT * FROM results")
    fun getResults() : LiveData<List<Results>>
}