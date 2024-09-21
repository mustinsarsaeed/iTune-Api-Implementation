package com.example.itune.api

import com.example.itune.model.TuneResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TuneApi {

    //For initial Favorite List for home Screen
    @GET("search")
    suspend fun getTuneList(
        @Query("term")
        term : String,
        @Query("country")
        country : String,
        @Query("media")
        media : String = "movie"
    ) : Response<TuneResponse>

    //For Search List
    @GET("search")
    suspend fun searchForTune(
        @Query("entity")
        term : String,
        @Query("country")
        country : String,
        @Query("term")
        searchQuery : String,
    ) : Response<TuneResponse>
}