package cz.vcelnicerudna.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cz.vcelnicerudna.R

class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.price_image)
    var titleView: TextView = itemView.findViewById(R.id.price_title)
    var descriptionView: TextView = itemView.findViewById(R.id.price_description)
    var button: Button = itemView.findViewById(R.id.reserve_button)
}