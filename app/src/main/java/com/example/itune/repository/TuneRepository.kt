package com.example.itune.repository

import com.example.itune.api.RetrofitInstance
import com.example.itune.db.ResultDatabase

class TuneRepository(
    val db : ResultDatabase
) {
    suspend fun getAllTuneList(term:String,countryList : String,media:String) =
        RetrofitInstance.api.getTuneList(term,countryList,media,)
}