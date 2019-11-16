package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.models.PhotoData

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo")
    fun getPhoto(): PhotoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoData: PhotoData)
}