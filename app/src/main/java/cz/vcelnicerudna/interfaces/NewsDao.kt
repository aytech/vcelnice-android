package cz.vcelnicerudna.interfaces

import androidx.room.*
import cz.vcelnicerudna.models.News
import io.reactivex.Single

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getNews(): Single<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: News): Single<Long>
}