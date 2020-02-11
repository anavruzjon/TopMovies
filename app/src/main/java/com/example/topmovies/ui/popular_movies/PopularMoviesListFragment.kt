package com.example.topmovies.ui.popular_movies

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker

import com.example.topmovies.R
import com.example.topmovies.ShowNotificationManager
import com.example.topmovies.adapter.movies_list.MoviesListAdapter
import com.example.topmovies.data.db.Movie
import com.example.topmovies.data.db.getDatabase
import com.example.topmovies.data.remote.ApiService
import com.example.topmovies.data.repositories.MovieRepository
import com.example.topmovies.databinding.FragmentPopularMoviesListBinding
import com.example.topmovies.listeners.MovieClickListener
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class PopularMoviesListFragment : Fragment() {

    companion object {

        const val MOVIE_TITLE_KEY = "movieTitle"
        const val MOVIE_ID_KEY = "movieId"

    }

    private lateinit var viewModel: PopularMoviesListViewModel
    private lateinit var binding: FragmentPopularMoviesListBinding
    private lateinit var adapter: MoviesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentPopularMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = (activity as AppCompatActivity).application
        val database = getDatabase(application)
        val network = ApiService.retrofitApi
        val repository = MovieRepository.getRepository(database, network, application)
        val viewModelFactory = PopularMovieListViewModelFactory(repository)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PopularMoviesListViewModel::class.java]

        viewModel.moviesList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.isInternetConnected.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!it)
                    Snackbar.make(
                        (activity as AppCompatActivity).findViewById(android.R.id.content),
                        getString(R.string.no_internet_connection),
                        Snackbar.LENGTH_LONG
                    ).show()
            }
        })

        viewModel.isDateTimePickerAndSchedule.observe(viewLifecycleOwner, Observer {
            it?.let { movie ->
                MaterialDialog(context!!).show {
                    dateTimePicker { _, datetime ->
                        val millis = datetime.timeInMillis
                        val systemTime = System.currentTimeMillis()
                        val diffTime = millis - systemTime
                        scheduleViewing(diffTime, movie)
                        viewModel.dismissDateTimePicker()
                    }.setOnCancelListener {
                        viewModel.dismissDateTimePicker()
                    }
                }
            }
        })

        setupRecycler()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

    private fun setupRecycler() {
        binding.moviesListRecycler.layoutManager = LinearLayoutManager(context)
        binding.moviesListRecycler.setHasFixedSize(true)

        adapter = MoviesListAdapter(listener = MovieClickListener {
            viewModel.showDateTimePickerAndSchedule(it)
        })

        binding.moviesListRecycler.adapter = adapter
    }

    private fun scheduleViewing(duration: Long, movie: Movie) {

        val data = Data.Builder()
            .putString(MOVIE_TITLE_KEY, movie.title)
            .putLong(MOVIE_ID_KEY, movie.id)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<ShowNotificationManager>()
            .setInputData(data)
            .setInitialDelay(duration, TimeUnit.MILLISECONDS)
            .build()

        val workManger = WorkManager.getInstance((activity as AppCompatActivity).applicationContext)
        workManger.enqueue(notificationWorker)

    }

}
