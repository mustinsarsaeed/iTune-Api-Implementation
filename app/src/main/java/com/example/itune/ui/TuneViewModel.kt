package com.example.itune.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itune.TuneResponse
import com.example.itune.repository.TuneRepository
import com.example.itune.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TuneViewModel(
    val repository : TuneRepository
) : ViewModel() {

    val tuneList : MutableLiveData<Resource<TuneResponse>> = MutableLiveData()

    init {
        getTuneList("star","au","movie")
    }
    fun getTuneList(term : String,country: String,media :String) = viewModelScope.launch {
        tuneList.postValue(Resource.Loading())
        val response = repository.getAllTuneList(term,country,media)
        tuneList.postValue(handleTuneResponse(response))
    }

    private fun handleTuneResponse(response: Response<TuneResponse>) : Resource<TuneResponse>{
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}