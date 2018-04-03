package cz.vcelnicerudna.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.text.Html.fromHtml
import android.text.Spanned

class News() : Parcelable {
    var id: Int = 0
    var title: String? = null
    var text: String? = null
    var icon: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        text = parcel.readString()
        icon = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<News> {
        override fun createFromParcel(parcel: Parcel): News {
            return News(parcel)
        }

        override fun newArray(size: Int): Array<News?> {
            return arrayOfNulls(size)
        }
    }

    fun getParsedText(): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            fromHtml(text)
        }
    }
}
