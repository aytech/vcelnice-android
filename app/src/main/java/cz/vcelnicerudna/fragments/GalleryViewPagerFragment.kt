package cz.vcelnicerudna.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.GalleryPagerAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo
import kotlinx.android.synthetic.main.fragment_gallery_view_pager.*

class GalleryViewPagerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPhoto: Int = arguments!!.getInt(StringConstants.PHOTO_POSITION)
        val photos: ArrayList<Photo> = arguments!!.getParcelableArrayList(StringConstants.PHOTOS_KEY)
        val galleryPagerAdapter = GalleryPagerAdapter(childFragmentManager, photos)

        photo_view_pager.adapter = galleryPagerAdapter
        photo_view_pager.currentItem = currentPhoto
    }

    companion object {
        fun newInstance(position: Int, data: ArrayList<Photo>): GalleryViewPagerFragment {
            val fragment = GalleryViewPagerFragment()
            val arguments = Bundle()
            arguments.putInt(StringConstants.PHOTO_POSITION, position)
            arguments.putParcelableArrayList(StringConstants.PHOTOS_KEY, data)
            fragment.arguments = arguments
            return fragment
        }
    }
}