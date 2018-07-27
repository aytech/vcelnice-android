package cz.vcelnicerudna.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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
