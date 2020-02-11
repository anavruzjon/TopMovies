package com.example.topmovies.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.data.db.Movie
import com.example.topmovies.data.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PopularMoviesListViewModel(private val repository: MovieRepository) : ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    val moviesList = repository.movesList

    val isRefreshing = repository.isRefreshing
    val isInternetConnected = repository.isInternetConnected

    private val _isDateTimePickerAndSchedule = MutableLiveData<Movie?>()
    val isDateTimePickerAndSchedule: LiveData<Movie?> = _isDateTimePickerAndSchedule

    init {
        viewModelScope.launch {
            repository.getPopularMovies(year = 2019, pageCount = 3)
        }
    }

    fun showDateTimePickerAndSchedule(movie: Movie) {
        _isDateTimePickerAndSchedule.value = movie
    }

    fun dismissDateTimePicker() {
        _isDateTimePickerAndSchedule.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
