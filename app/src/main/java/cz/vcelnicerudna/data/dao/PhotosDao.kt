package cz.vcelnicerudna.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.data.model.Photo
import io.reactivex.Single

@Dao
interface PhotosDao {
    @Query("SELECT * FROM photos")
    fun getPhotos(): Single<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo) : Single<Long>
}