package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import cz.vcelnicerudna.models.HomeText

@Dao
interface HomeDao {
    @Query("SELECT * FROM home_text")
    fun getHomeText(): HomeText

    @Insert(onConflict = REPLACE)
    fun insert(homeText: HomeText)

    @Query("DELETE FROM home_text")
    fun deleteAll()
}