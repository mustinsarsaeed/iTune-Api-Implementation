package com.example.itune.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itune.R
import com.example.itune.adapter.ResultsAdapter
import com.example.itune.databinding.FragmentHomeBinding
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel
import com.example.itune.util.Resource

class HomeFragment : Fragment(R.layout.fragment_home) {
    val TAG = "HomeFragment"

    lateinit var viewModel : TuneViewModel
    lateinit var resultAdapter : ResultsAdapter
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as TuneActivity).viewModel
        setUpRecyclerView()
        observeViewModalData()
    }

    private fun observeViewModalData() {
        viewModel.tuneList.observe(viewLifecycleOwner) { response ->
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
                        Log.e(TAG, "Error Occured $message")

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
        binding.rvBreakingNews.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(activity)

        }


    }
}