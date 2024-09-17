package com.example.itune.api

import com.example.itune.TuneResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TuneApi {

    //For initial Favorite List
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
    @GET("all")
    suspend fun searchForTune(
        @Query("q")
        searchQuery : String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String
    ) : Response<TuneResponse>
}