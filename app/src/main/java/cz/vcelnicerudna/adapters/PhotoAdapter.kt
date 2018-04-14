package cz.vcelnicerudna.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.GlideApp
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.Photo

class PhotoAdapter(var context: Context, private var dataSet: Array<Photo>): RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val textView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_photo, parent, false) as View
        return PhotoViewHolder(textView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item: Photo = dataSet[position]
        if (item.thumb != "") {
            Log.d("PhotoAdapter", item.thumb)
            GlideApp
                    .with(context)
                    .load(APIConstants.VCELNICE_BASE_URL + item.thumb)
                    .placeholder(R.mipmap.ic_bee)
                    .fitCenter()
                    .into(holder.imageView)
        }
    }

    fun loadNewData(photo: Array<Photo>) {
        this.dataSet = photo
        notifyDataSetChanged()
    }
}