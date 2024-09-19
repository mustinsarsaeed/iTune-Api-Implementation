package com.example.itune.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.itune.R
import com.example.itune.adapter.ResultsAdapter
import com.example.itune.databinding.FragmentDetailBinding
import com.example.itune.databinding.FragmentHomeBinding
import com.example.itune.model.Results
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val tag = "DetailFragment"
    private lateinit var viewModel: TuneViewModel
    private lateinit var binding: FragmentDetailBinding
    // val args : DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as TuneActivity).viewModel
        val tune = arguments?.getSerializable("tunes") as? Results
        // Now you can access the properties of the `tune` object
        tune?.trackId?.let { viewModel.isMovieInFavorites(it).observe(viewLifecycleOwner, Observer { isFavorite ->
            if (isFavorite) {
                binding.favoriteMovie.setImageResource(R.drawable.ic_fav_selected)
            } else {
                binding.favoriteMovie.setImageResource(R.drawable.ic_fav_unselected)
            }

        }) }
        tune?.let {
            binding.tvTitle.text = tune.trackName
            binding.tvGenre.text = tune.primaryGenreName
            binding.tvPrice.text = tune.trackPrice.toString()
            binding.tvLongDescription.text = tune.longDescription
            Glide.with(this).load(tune.artworkUrl100).into(binding.ivMovieImage)
            binding.favoriteMovie.setOnClickListener {
                tune.trackId?.let { it1 ->
                    viewModel.isMovieInFavorites(it1)
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
                                viewModel.saveResult(tune)
                                binding.favoriteMovie.setImageResource(R.drawable.ic_fav_selected)
                                Snackbar.make(
                                    view,
                                    getString(R.string.movie_saved_successfully),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        }
    }
}