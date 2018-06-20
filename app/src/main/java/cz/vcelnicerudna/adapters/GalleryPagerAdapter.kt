package cz.vcelnicerudna.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cz.vcelnicerudna.fragments.PhotoDetailFragment
import cz.vcelnicerudna.models.Photo

class GalleryPagerAdapter(fm: FragmentManager, data: ArrayList<Photo>) : FragmentStatePagerAdapter(fm) {

    private var photos: ArrayList<Photo> = data

    override fun getItem(position: Int): Fragment {
        return PhotoDetailFragment.newInstance(photos[position])
    }

    override fun getCount(): Int {
        return photos.size
    }

}