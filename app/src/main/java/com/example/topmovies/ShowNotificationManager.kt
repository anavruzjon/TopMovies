package com.example.topmovies

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.topmovies.ui.popular_movies.PopularMoviesListFragment

class ShowNotificationManager(applicationContext: Context, workerParms: WorkerParameters) : Worker(applicationContext, workerParms) {
    override fun doWork(): Result {

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val movieId = inputData.getLong(PopularMoviesListFragment.MOVIE_ID_KEY, 0).toInt()
        val movieTitle = inputData.getString(PopularMoviesListFragment.MOVIE_TITLE_KEY)

        val activityIntent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(applicationContext, 0, activityIntent, 0)

        val notification = NotificationCompat.Builder(applicationContext, App.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black)
            .setContentTitle(movieTitle)
            .setContentText("You scheduled to view the $movieTitle. It's time to watch it.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(contentIntent)
            .build()

        notificationManager.notify(movieId, notification)


        return Result.success()
    }

}
