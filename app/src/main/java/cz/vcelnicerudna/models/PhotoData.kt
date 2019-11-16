package cz.vcelnicerudna.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cz.vcelnicerudna.converters.PhotoConverter

@Entity(tableName = "photo")
class PhotoData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(PhotoConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Photo> = arrayOf()
}