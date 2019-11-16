package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.models.LocationData

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations")
    fun getLocations(): LocationData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationData: LocationData)
}