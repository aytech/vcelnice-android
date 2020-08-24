package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.models.Location
import io.reactivex.Single

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations")
    fun getLocations(): Single<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location): Single<Long>
}