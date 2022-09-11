package ru.mts.data.news.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAll(): List<NewsEntity>

    @Query("SELECT * FROM news WHERE id = :id")
    fun getById(id: Long): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: NewsEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<NewsEntity>)

    @Update
    fun update(news: NewsEntity?)

    @Delete
    fun delete(news: NewsEntity?)
}