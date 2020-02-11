package com.example.topmovies.utils

import android.content.Context

fun Context.dpToPx(dp: Int) = dp.toFloat() * this.resources.displayMetrics.density
fun Context.spToPx(sp: Int) = sp.toFloat() * this.resources.displayMetrics.scaledDensity