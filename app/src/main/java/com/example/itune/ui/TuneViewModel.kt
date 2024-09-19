package com.example.itune.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.itune.TuneApplication
import com.example.itune.model.Results
import com.example.itune.model.TuneResponse
import com.example.itune.repository.TuneRepository
import com.example.itune.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class TuneViewModel(
    app:Application,
    private val repository : TuneRepository
) : AndroidViewModel(app) {
    val tag = "TuneViewModel"
    val tuneList : MutableLiveData<Resource<TuneResponse>> = MutableLiveData()

    val searchList : MutableLiveData<Resource<TuneResponse>> = MutableLiveData()


    init {
        getTuneList("star","au","movie")
    }
    fun getTuneList(term : String,country: String,media :String) = viewModelScope.launch {
        safeTuneCall(term,country,media)
    }

    fun searchNews(entity:String,country:String,searchQuery:String) = viewModelScope.launch {
        searchList.postValue(Resource.Loading())
        val response = repository.searchNews(entity,country,searchQuery)
        searchList.postValue(handleSearchTuneResponse(response))
    }
    private fun handleTuneResponse(response: Response<TuneResponse>) : Resource<TuneResponse>{
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchTuneResponse(response: Response<TuneResponse>) : Resource<TuneResponse>{
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveResult(result: Results) = viewModelScope.launch {
        repository.upsert(result)
    }

    fun getSavedResult() = repository.getSaveResult()

    fun deleteResult(result: Results) = viewModelScope.launch {
        repository.deleteResults(result)
    }

    fun isMovieInFavorites(trackId: Int): LiveData<Boolean> {
        return liveData {
            val isFavorite = repository.isMovieInFavorites(trackId) // Query the DB
            emit(isFavorite)
        }
    }


    private suspend fun safeTuneCall(term: String,country:String,media:String) {
        tuneList.postValue(Resource.Loading())
        try {
            if(isInternetAvailable()) {
                val response = repository.getAllTuneList(term,country,media)
                tuneList.postValue(handleTuneResponse(response))
            } else {
                Log.d(tag, "safeTuneCall: No Internet Connection")
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> tuneList.postValue(Resource.Error("Network Failure"))
                else -> tuneList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getApplication<TuneApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}