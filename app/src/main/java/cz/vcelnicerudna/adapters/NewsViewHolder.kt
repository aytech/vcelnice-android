package cz.vcelnicerudna.adapters

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import cz.vcelnicerudna.R.id.news_title
import kotlinx.android.synthetic.main.fragment_news.view.*

class NewsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    init {
        Log.d("NewsViewHolder", "Loading data: $itemView")
    }
}