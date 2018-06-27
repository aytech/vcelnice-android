package cz.vcelnicerudna.models

import android.os.Parcel
import android.os.Parcelable

class Photo() : Parcelable {
    var id: Int = 0
    var caption: String = ""
    var image: String = ""
    var thumb: String = ""
    var width: Int = 0
    var height: Int = 0
    var created: String = ""

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