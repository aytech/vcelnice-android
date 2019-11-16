package cz.vcelnicerudna.adapters

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.core.view.ViewCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
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
                .load(APIConstants.VCELNICE_BASE_URL + photo.image)
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
                        val snackbar = Snackbar.make(
                                imageView,
                                activity.applicationContext.getString(R.string.network_error),
                                Snackbar.LENGTH_LONG)
                        snackbar.show()
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
