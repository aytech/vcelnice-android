package cz.vcelnicerudna.fragments

//import com.bumptech.glide.request.target.SimpleTarget
//import com.bumptech.glide.request.transition.Transition
//import cz.vcelnicerudna.GlideApp
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo

class PhotoDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo: Photo = arguments!!.getParcelable(StringConstants.PHOTO_KEY)

//        GlideApp
//                .with(activity as FragmentActivity)
//                .load(APIConstants.VCELNICE_BASE_URL + photo.image)
//                .into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        photo_view_detail.setImageDrawable(resource)
//                    }
//                })
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