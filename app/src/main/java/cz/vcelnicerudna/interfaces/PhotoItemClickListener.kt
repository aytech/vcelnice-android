package cz.vcelnicerudna.interfaces

import android.widget.ImageView
import cz.vcelnicerudna.models.Photo

interface PhotoItemClickListener {
    fun onPhotoItemClickListener(position: Int, photo: Photo, imageView: ImageView)
}