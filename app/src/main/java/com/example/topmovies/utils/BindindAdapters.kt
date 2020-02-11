package com.example.topmovies.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.topmovies.ui.custom_views.VoteRating

private const val BASE_URL = "https://image.tmdb.org/t/p/"
private const val FILE_SIZE = "w300"

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(posterPath: String) {
    val stringUrl = "$BASE_URL$FILE_SIZE/$posterPath"
    Glide.with(this).load(stringUrl).into(this)
}

@BindingAdapter("setPercentage")
fun VoteRating.setPercentage(percent: Int) {
    this.setPercentageValue(percent)
}


@BindingAdapter("setFormattedDate")
fun TextView.setFormattedDate(date: String){
    this.text = DateConverter.formatDate(date)
}