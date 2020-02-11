package com.example.topmovies.ui.popular_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.topmovies.data.repositories.MovieRepository

class PopularMovieListViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PopularMoviesListViewModel::class.java))
            return PopularMoviesListViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}