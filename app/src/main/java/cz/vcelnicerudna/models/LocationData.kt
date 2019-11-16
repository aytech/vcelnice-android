package cz.vcelnicerudna.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cz.vcelnicerudna.converters.LocationConverter

@Entity(tableName = "locations")
class LocationData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(LocationConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Location> = arrayOf()
}