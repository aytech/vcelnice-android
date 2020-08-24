package cz.vcelnicerudna.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
class Location {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "address")
    var address: String = ""
}