package com.example.itune.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itune.R
import com.example.itune.model.Results
import com.example.itune.ui.TuneActivity
import com.example.itune.ui.TuneViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    inner class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val favoriteMovie: ImageView = itemView.findViewById(R.id.favoriteMovie)
        private val lastVisit: TextView = itemView.findViewById(R.id.tvLastVisit)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(results: Results, viewModel: TuneViewModel) {
            Glide.with(itemView).load(results.artworkUrl100)
                .into(itemView.findViewById(R.id.ivMovieImage))
            itemView.findViewById<TextView>(R.id.tvTitle).text = results.trackName
            itemView.findViewById<TextView>(R.id.tvPrice).text = results.trackPrice.toString()
            itemView.findViewById<TextView>(R.id.tvGenre).text = results.primaryGenreName


            //Update Favorite Icon Observe Live Data
            results.trackId?.let {
                viewModel.isMovieInFavorites(it)
                    .observe(itemView.context as LifecycleOwner) { isFavorite ->
                        favoriteMovie.setImageResource(if (isFavorite) R.drawable.ic_fav_selected else R.drawable.ic_fav_unselected)
                    }
                // Show last visit date if it exists
                val lastVisitDate = getLastVisitDate(itemView.context, it)
                if (lastVisitDate != null) {
                    lastVisit.text = "Last visited: $lastVisitDate"
                } else {
                    lastVisit.text = ""
                }
            }

            // Set item click listener for the entire item
            itemView.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(results)
                }
                // Save the current date as the last visit date
                saveLastVisitDate(itemView.context, results.trackId)
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

    // Save last visit date to SharedPreferences
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveLastVisitDate(context: Context, trackId: Int?) {
        val sharedPreferences = context.getSharedPreferences("LastVisitPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        trackId?.let {
            editor.putString(it.toString(), currentDate)
            editor.apply()
        }
    }

    // Retrieve last visit date from SharedPreferences
    private fun getLastVisitDate(context: Context, trackId: Int?): String? {
        val sharedPreferences = context.getSharedPreferences("LastVisitPrefs", Context.MODE_PRIVATE)
        return trackId?.let { sharedPreferences.getString(it.toString(), null) }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val results = differ.currentList[position]
        holder.bind(results, (holder.itemView.context as TuneActivity).viewModel)
    }

    //Listener For Whole Item Click
    private var onItemClickListener: ((Results) -> Unit)? = null
    fun setOnItemClickListener(listener: (Results) -> Unit) {
        onItemClickListener = listener
    }

    //Listener For favorite button clicks
    private var onItemFavButton: ((Results) -> Unit)? = null
    fun setOnItemFavButton(listener: (Results) -> Unit) {
        onItemFavButton = listener
    }
}