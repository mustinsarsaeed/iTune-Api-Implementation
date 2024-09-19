package com.example.itune.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itune.repository.TuneRepository

class TuneViewModelProviderFactory(
    val app: Application,
    val repository: TuneRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        return TuneViewModel(app,repository) as T
    }
}