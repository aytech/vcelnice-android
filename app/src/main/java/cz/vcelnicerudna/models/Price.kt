package cz.vcelnicerudna.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.vcelnicerudna.configuration.StringConstants

@Entity(tableName = "prices")
class Price() : Parcelable {

    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = ""

    @ColumnInfo(name = "price")
    var price: Int = 0

    @ColumnInfo(name = "weight")
    var weight: String? = ""

    @ColumnInfo(name = "image")
    var image: String? = ""

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        price = parcel.readInt()
        weight = parcel.readString()
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(price)
        parcel.writeString(weight)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Price> {
        override fun createFromParcel(parcel: Parcel): Price {
            return Price(parcel)
        }

        override fun newArray(size: Int): Array<Price?> {
            return arrayOfNulls(size)
        }
    }

    fun getShortStringRepresentation(): String {
        return "$price ${StringConstants.CZ_CURRENCY} ($weight)"
    }
}