package cz.vcelnicerudna.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import cz.vcelnicerudna.converters.LocationConverter

@Entity(tableName = "locations")
class LocationData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(LocationConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Location> = arrayOf()
}