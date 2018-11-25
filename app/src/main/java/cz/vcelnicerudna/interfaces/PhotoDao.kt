package cz.vcelnicerudna.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cz.vcelnicerudna.models.PhotoData

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo")
    fun getPhoto(): PhotoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoData: PhotoData)
}