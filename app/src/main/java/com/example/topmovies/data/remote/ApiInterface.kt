package com.example.topmovies.data.remote


import com.example.topmovies.BuildConfig
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("discover/movie")
    fun fetchPopularMoviesAsync(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("primary_release_year") releaseYear: Int = 2019,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("page") page: Int = 1
    ): Deferred<MovieListResponse>
}