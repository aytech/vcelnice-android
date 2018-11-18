package cz.vcelnicerudna.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import cz.vcelnicerudna.converters.PhotoConverter

@Entity(tableName = "photo")
class PhotoData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(PhotoConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Photo> = arrayOf()
}