package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import cz.vcelnicerudna.models.HomeText
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface HomeDao {
    @Query("SELECT * FROM home_text")
    fun getHomeText(): Single<HomeText>

    // https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
    @Insert(onConflict = REPLACE)
    fun insert(homeText: HomeText)

    @Query("DELETE FROM home_text")
    fun deleteAll()
}