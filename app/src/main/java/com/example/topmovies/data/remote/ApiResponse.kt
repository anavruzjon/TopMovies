package com.example.topmovies.data.remote

import com.example.topmovies.data.db.Movie

data class MovieListResponse(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<MovieResponse>
)

data class MovieResponse(
    val id: Long,
    val poster_path: String?,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val original_title: String,
    val original_language: String,
    val title: String,
    val backdrop_path: String?,
    val popularity: Double,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Double
)

fun List<MovieResponse>.asDatabaseModelList(): List<Movie> {
    val result = mutableListOf<Movie>()
    this.forEach {
        val movie = Movie(
            id = it.id,
            posterPath = it.poster_path,
            adult = it.adult,
            overview = it.overview,
            releaseDate = it.release_date,
            originalTitle = it.original_title,
            originalLanguage = it.original_language,
            title = it.title,
            backdropPath = it.backdrop_path,
            popularity = it.popularity,
            voteCount = it.vote_count,
            video = it.video,
            voteAverage = it.vote_average
        )
        result.add(movie)
    }
    return result
}