package cz.vcelnicerudna.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.vcelnicerudna.R.id.*

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(news_icon)
    var titleView: TextView = itemView.findViewById(news_title)
    var descriptionView: TextView = itemView.findViewById(news_description)
}