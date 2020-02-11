package com.example.topmovies.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.data.db.Movie
import com.example.topmovies.data.db.MoviesDatabase
import com.example.topmovies.data.remote.ApiInterface
import com.example.topmovies.data.remote.asDatabaseModelList
import com.example.topmovies.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MovieRepository private constructor(
    private val application: Application,
    private val database: MoviesDatabase,
    private val network: ApiInterface
) {

    private val _isInternetConnected = MutableLiveData<Boolean>()
    val isInternetConnected: LiveData<Boolean> = _isInternetConnected

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    val movesList = database.movieDao.moviesList()

    suspend fun getPopularMovies(year: Int, pageCount: Int) {
        if (NetworkUtils.isConnectedInternet(application)) {
            _isInternetConnected.value = true
            _isRefreshing.value = true
            withContext(Dispatchers.IO) {
                try {
                    val result = mutableListOf<Movie>()

                    repeat(pageCount) {
                        val networkResult = network.fetchPopularMoviesAsync(releaseYear = year, page = (it + 1)).await()
                        result.addAll(networkResult.results.asDatabaseModelList())
                    }

                    database.movieDao.clear()
                    database.movieDao.insert(result)

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isRefreshing.postValue(false)
                }
            }
        } else {
            _isInternetConnected.value = false
        }
    }

    companion object {
        private var INSTANCE: MovieRepository? = null
        fun getRepository(database: MoviesDatabase, network: ApiInterface, application: Application): MovieRepository {
            synchronized(this) {
                if (INSTANCE == null)
                    INSTANCE = MovieRepository(application, database, network)
                return INSTANCE as MovieRepository
            }
        }
    }
}