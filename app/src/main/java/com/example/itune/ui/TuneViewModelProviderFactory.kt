package com.example.itune.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itune.repository.TuneRepository

class TuneViewModelProviderFactory(
    val repository: TuneRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        return TuneViewModel(repository) as T
    }
}