package com.example.topmovies.listeners

import com.example.topmovies.data.db.Movie

class MovieClickListener(val listener: (Movie) -> Unit) {
    fun onClickScheduling(item: Movie) = listener(item)
}