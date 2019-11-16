package cz.vcelnicerudna.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cz.vcelnicerudna.converters.PricesConverter

@Entity(tableName = "prices")
class PricesData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(PricesConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Price> = arrayOf()
}