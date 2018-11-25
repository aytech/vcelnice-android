package cz.vcelnicerudna.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cz.vcelnicerudna.models.LocationData

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations")
    fun getLocations(): LocationData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationData: LocationData)
}