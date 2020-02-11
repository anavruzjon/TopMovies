package com.example.topmovies.adapter.movies_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.topmovies.data.db.Movie
import com.example.topmovies.databinding.ItemMovieListBinding
import com.example.topmovies.listeners.MovieClickListener

class MoviesListAdapter(private val listener: MovieClickListener) : ListAdapter<Movie, RecyclerView.ViewHolder>(MoviesListItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(getItem(position), listener)
    }

    class MovieViewHolder private constructor(private val binding: ItemMovieListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, listener: MovieClickListener) {
            binding.item = item
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieListBinding.inflate(inflater, parent, false)
                return MovieViewHolder(binding)
            }
        }

    }

}