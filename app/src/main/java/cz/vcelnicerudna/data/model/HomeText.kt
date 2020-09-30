package cz.vcelnicerudna.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_text")
class HomeText {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "text")
    var text: String = ""

    @ColumnInfo(name = "icon")
    var icon: String = ""
}
