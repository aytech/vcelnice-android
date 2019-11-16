package cz.vcelnicerudna.models

import androidx.room.*
import cz.vcelnicerudna.converters.NewsConverter

@Entity(tableName = "news")
class NewsData {
    @PrimaryKey
    var id: Int = 0

    @TypeConverters(NewsConverter::class)
    @ColumnInfo(name = "data")
    var data: Array<News> = arrayOf()
}