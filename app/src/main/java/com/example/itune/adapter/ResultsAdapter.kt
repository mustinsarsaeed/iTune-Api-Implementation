package com.example.itune.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itune.R
import com.example.itune.Results

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>()  {


    inner class ResultsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.trackId == newItem.trackId
            Log.d("ResultsAdapter", "SameItem: ${oldItem.trackId}")
            Log.d("ResultsAdapter", "SameItem: ${newItem.trackId}")

        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
            Log.d("ResultsAdapter", "Condition: ${oldItem == newItem}")

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
        Log.d("ResultsAdapter", "onCreateViewHolder: ")

    }

    override fun getItemCount(): Int {
        Log.d("ResultsAdapter", "ItemCount: ${differ.currentList.size}")

        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val results = differ.currentList[position]
        Log.d("ResultsAdapter", "Binding data: ${results.trackName}")

        holder.itemView.apply {
            Glide.with(this).load(results.artworkUrl100).into(findViewById(R.id.ivMovieImage))
            Log.d("ResultAdapter", "onBindViewHolder: ${results.trackName}")
            findViewById<TextView>(R.id.tvTitle).text = results.trackName
            findViewById<TextView>(R.id.tvPrice).text = results.trackPrice.toString()
            findViewById<TextView>(R.id.tvGenre).text = results.primaryGenreName
            setOnItemClickListener {
                onItemClickListener?.let {
                    it(results)
                }
            }
        }
    }

    private var onItemClickListener : ((Results) -> Unit)? = null
    fun setOnItemClickListener(listener : (Results) -> Unit) {
        onItemClickListener = listener
    }
}