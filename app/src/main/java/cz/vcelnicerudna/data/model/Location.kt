package cz.vcelnicerudna.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vcelnicerudna.configuration.AppConstants.Companion.DEFAULT_LOCATION

@Entity(tableName = "locations")
class Location {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "address")
    var address: String = ""

    override fun toString(): String {
        return address
    }

    companion object {
        fun default(): Location {
            val location = Location()
            location.address = DEFAULT_LOCATION
            return location
        }
    }
}