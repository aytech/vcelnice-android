package cz.vcelnicerudna.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.PhotoAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.interfaces.PhotoItemClickListener
import cz.vcelnicerudna.models.Photo
import kotlinx.android.synthetic.main.fragment_photo_recycler_view.*

class PhotoRecyclerViewFragment : Fragment(), PhotoItemClickListener {

    private var photos: ArrayList<Photo> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo_recycler_view.layoutManager = GridLayoutManager(view.context, 3)
        photo_recycler_view.adapter = PhotoAdapter(view.context, photos, this)
    }

    override fun onPhotoItemClickListener(position: Int, photo: Photo, imageView: ImageView) {
        ViewCompat.setTransitionName(imageView, photo.caption)
        fragmentManager
                ?.beginTransaction()
                ?.addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                ?.addToBackStack(PhotoRecyclerViewFragment::class.java.simpleName)
                ?.replace(R.id.drawer_layout, GalleryViewPagerFragment.newInstance(position, photos))
                ?.commit()

    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (args != null) {
            photos = args.getParcelableArrayList(StringConstants.PHOTOS_KEY)
        }
    }
}