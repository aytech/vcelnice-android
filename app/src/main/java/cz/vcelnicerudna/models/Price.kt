package cz.vcelnicerudna.models

import android.os.Parcel
import android.os.Parcelable
import cz.vcelnicerudna.configuration.StringConstants

class Price() : Parcelable {
    var title: String? = null
    var price: Int = 0
    var weight: String? = null
    var image: String? = null

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

    fun getStringRepresentation(): String {
        return "$title ($weight) - $price ${StringConstants.CZ_CURRENCY}"
    }

    fun getShortStringRepresentation(): String {
        return "$price ${StringConstants.CZ_CURRENCY} ($weight)"
    }
}