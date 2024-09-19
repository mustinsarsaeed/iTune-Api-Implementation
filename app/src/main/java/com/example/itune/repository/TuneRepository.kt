package com.example.itune.repository

import com.example.itune.api.RetrofitInstance
import com.example.itune.db.ResultDatabase
import com.example.itune.model.Results

class TuneRepository(
    private val db : ResultDatabase
) {
    suspend fun getAllTuneList(term:String,countryList : String,media:String) =
        RetrofitInstance.api.getTuneList(term,countryList,media)

    suspend fun searchNews(entity:String,country:String,searchQuery:String) =
        RetrofitInstance.api.searchForTune(entity,country,searchQuery)

    suspend fun upsert(result: Results) = db.getResultsDao().upsert(result)

    fun getSaveResult() = db.getResultsDao().getResults()

 //   suspend fun deleteResults(result: Results) = db.getResultsDao().deleteResult(result)

    suspend fun isMovieInFavorites(trackId: Int): Boolean {
        return db.getResultsDao().isMovieInFavorites(trackId)
    }
}