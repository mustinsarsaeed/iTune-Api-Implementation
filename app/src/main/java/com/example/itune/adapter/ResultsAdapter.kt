package com.example.itune.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itune.R
import com.example.itune.model.Results
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>()  {

    inner class ResultsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val favoriteMovie: ImageView = itemView.findViewById(R.id.favoriteMovie)

        fun bind(results: Results, viewModel: TuneViewModel) {
            Glide.with(itemView).load(results.artworkUrl100).into(itemView.findViewById(R.id.ivMovieImage))
            itemView.findViewById<TextView>(R.id.tvTitle).text = results.trackName
            itemView.findViewById<TextView>(R.id.tvPrice).text = results.trackPrice.toString()
            itemView.findViewById<TextView>(R.id.tvGenre).text = results.primaryGenreName


            //Update Favorite Icon Observe Live Data
            results.trackId?.let {
                viewModel.isMovieInFavorites(it).observe(itemView.context as LifecycleOwner) { isFavorite ->
                    favoriteMovie.setImageResource(if (isFavorite) R.drawable.ic_fav_selected else R.drawable.ic_fav_unselected)
                }
            }

            // Set item click listener for the entire item
            itemView.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(results)
                }
            }

            // Handle the click event for the favorite button
            favoriteMovie.setOnClickListener {
                onItemFavButton?.let { favClick ->
                    favClick(results)
                    favoriteMovie.setImageResource(R.drawable.ic_fav_selected)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.trackId == newItem.trackId
        }
        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_result_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val results = differ.currentList[position]
        holder.bind(results, (holder.itemView.context as TuneActivity).viewModel)
    }

    //Listener For Whole Item Click
    private var onItemClickListener : ((Results) -> Unit)? = null
    fun setOnItemClickListener(listener : (Results) -> Unit) {
        onItemClickListener = listener
    }

    //Listener For favorite button clicks
    private var onItemFavButton: ((Results) -> Unit)? = null
    fun setOnItemFavButton(listener: (Results) -> Unit) {
        onItemFavButton = listener
    }
}