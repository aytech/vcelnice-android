package cz.vcelnicerudna.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
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