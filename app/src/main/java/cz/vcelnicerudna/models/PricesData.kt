package cz.vcelnicerudna.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import cz.vcelnicerudna.converters.PricesConverter

@Entity(tableName = "prices")
class PricesData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(PricesConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<Price> = arrayOf()
}