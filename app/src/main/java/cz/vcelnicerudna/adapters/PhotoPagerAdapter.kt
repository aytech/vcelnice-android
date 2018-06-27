package cz.vcelnicerudna.adapters

import android.app.Activity
import android.support.v4.app.ActivityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.models.Photo

class PhotoPagerAdapter(
        val activity: Activity,
        private val photos: ArrayList<Photo>,
        var currentPosition: Int)
    : PagerAdapter() {

    private val views = SparseArray<View?>(photos.size)

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val photo = photos[position]
        val imageView = ImageView(collection.context)
        ViewCompat.setTransitionName(imageView, Photo.transitionName(photo.id))
        views.put(position, imageView)

        Picasso
                .with(collection.context)
                .load(photo.image)
                .noFade()
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        if (position == currentPosition) {
                            imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                                    ActivityCompat.startPostponedEnterTransition(activity)
                                    return true
                                }
                            })
                        }
                    }

                    override fun onError() {
                        ActivityCompat.startPostponedEnterTransition(activity)
                    }
                })

        collection.addView(imageView)
        return imageView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        views.removeAt(position)
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = photos.size

    fun getView(position: Int): View? = views.get(position)
}
