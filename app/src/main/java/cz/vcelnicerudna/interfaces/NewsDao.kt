package cz.vcelnicerudna.interfaces

import androidx.room.*
import cz.vcelnicerudna.models.NewsData

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getNews(): NewsData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: NewsData)
}