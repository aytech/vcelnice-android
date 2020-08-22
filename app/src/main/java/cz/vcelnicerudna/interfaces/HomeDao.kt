package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import cz.vcelnicerudna.models.HomeText
import io.reactivex.Single

@Dao
interface HomeDao {
    @Query("SELECT * FROM home_text")
    fun getHomeText(): Single<HomeText>

    @Insert(onConflict = REPLACE)
    fun insert(homeText: HomeText): Single<Long>

    @Query("DELETE FROM home_text")
    fun deleteAll(): Single<Int>
}