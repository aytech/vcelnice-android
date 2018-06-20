package cz.vcelnicerudna.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo

class PhotoDetailFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("PhotoDetailFragment", "View created: ")
    }

    companion object {
        fun newInstance(photo: Photo): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable(StringConstants.PHOTO_KEY, photo)
            fragment.arguments = arguments
            return fragment
        }
    }
}