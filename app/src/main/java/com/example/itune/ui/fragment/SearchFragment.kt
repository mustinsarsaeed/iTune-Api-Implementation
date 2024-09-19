package com.example.itune.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itune.R
import com.example.itune.adapter.ResultsAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.itune.databinding.FragmentSearchBinding
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel
import com.example.itune.util.Constant.Companion.SEARCH_TIME_DELAY
import com.example.itune.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val tag = "SearchFragment"
    private lateinit var resultAdapter: ResultsAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: TuneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as TuneActivity).viewModel
        setUpRecyclerView()
        observeViewModalData()
        resultAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("tunes", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
        }
        resultAdapter.setOnItemFavButton { result ->
            result.trackId?.let {
                viewModel.isMovieInFavorites(it)
                    .observe(viewLifecycleOwner, Observer { isFavorite ->
                        if (isFavorite) {
                            // Movie is already in favorites, show a toast and set the icon to selected
                            Snackbar.make(
                                view,
                                getString(R.string.already_added_to_favorites),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            // Movie is not in favorites, add it and set the icon to selected
                            viewModel.saveResult(result)
                            Snackbar.make(
                                view,
                                getString(R.string.movie_saved_successfully),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

        }
        var job: Job? = null
        binding.etSearchView.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                if(isInternetAvailable()) {
                    viewModel.searchNews("movie", "au", editable.toString())
                } else {
                    Snackbar.make(
                        view,
                        getString(R.string.no_internet),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeViewModalData() {
        viewModel.searchList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { tuneResponse ->
                        resultAdapter.differ.submitList(tuneResponse.results)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(tag, "Error Occured $message")

                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        resultAdapter = ResultsAdapter()
        binding.rvSearchMovie.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

    private fun getSavedData() {
        hideProgressBar()
        // Show locally saved data if there's no internet
        viewModel.getSavedResult().observe(viewLifecycleOwner, Observer { result ->
            resultAdapter.differ.submitList(result)
        })
        Toast.makeText(activity, "No internet connection, showing saved data.", Toast.LENGTH_LONG)
            .show()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}