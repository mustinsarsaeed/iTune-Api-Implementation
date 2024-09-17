package com.example.itune.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.itune.R
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var viewModel : TuneViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as TuneActivity).viewModel
    }
}