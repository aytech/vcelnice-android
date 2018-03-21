package cz.vcelnicerudna.adapters

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.View

class NewsViewHolder() : RecyclerView.ViewHolder(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsViewHolder> {
        override fun createFromParcel(parcel: Parcel): NewsViewHolder {
            return NewsViewHolder(parcel)
        }

        override fun newArray(size: Int): Array<NewsViewHolder?> {
            return arrayOfNulls(size)
        }
    }

}