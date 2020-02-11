package com.example.topmovies.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("delete from movie")
    fun clear()

    @Query("select count(id) from movie")
    fun count(): Int

    @Query("select * from movie")
    fun moviesList(): LiveData<List<Movie>>

    @Insert(onConflict = REPLACE)
    fun insert(list: List<Movie>)
}