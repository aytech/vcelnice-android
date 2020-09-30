package cz.vcelnicerudna.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
class Photo() : Parcelable {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "caption")
    var caption: String? = ""

    @ColumnInfo(name = "image")
    var image: String? = ""

    @ColumnInfo(name = "thumb")
    var thumb: String? = ""

    @ColumnInfo(name = "width")
    var width: Int = 0

    @ColumnInfo(name = "height")
    var height: Int = 0

    @ColumnInfo(name = "created")
    var created: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        caption = parcel.readString()
        image = parcel.readString()
        thumb = parcel.readString()
        width = parcel.readInt()
        height = parcel.readInt()
        created = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(caption)
        parcel.writeString(image)
        parcel.writeString(thumb)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeString(created)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }

        fun transitionName(id: Int) = "item_$id"
    }
}