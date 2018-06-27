package cz.vcelnicerudna

//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import cz.vcelnicerudna.R.layout.activity_photo_view
import cz.vcelnicerudna.adapters.PhotoPagerAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo
import kotlinx.android.synthetic.main.activity_photo_view.*

class PhotoViewActivity : AppCompatActivity() {

    private lateinit var photos: ArrayList<Photo>

    companion object {
        const val ITEM_ID = "itemId"
        private const val SAVED_CURRENT_PAGE_POSITION = "current_page_position"
    }

    private var isReturning: Boolean = false
    private var startingPosition: Int = 0
    private var currentPosition: Int = 0
    private var photoPagerAdapter: PhotoPagerAdapter? = null

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (isReturning) {
                val sharedElement = photoPagerAdapter?.getView(currentPosition)

                if (startingPosition != currentPosition) {
                    names.clear()
                    names.add(ViewCompat.getTransitionName(sharedElement))

                    sharedElements.clear()
                    sharedElements[ViewCompat.getTransitionName(sharedElement)] = sharedElement!!
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_photo_view)

        photos = intent.getParcelableArrayListExtra(StringConstants.PHOTOS_KEY)

        ActivityCompat.postponeEnterTransition(this)
        ActivityCompat.setEnterSharedElementCallback(this, enterElementCallback)
        setupToolBar()

        val index = photos.indexOfFirst { it.id == intent.getIntExtra(ITEM_ID, 0) }
        startingPosition = if (index > 0) index else 0
        currentPosition = savedInstanceState?.getInt(SAVED_CURRENT_PAGE_POSITION) ?: startingPosition

        photoPagerAdapter = PhotoPagerAdapter(this, photos, currentPosition)
        viewPager.adapter = photoPagerAdapter
        viewPager.currentItem = currentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(SAVED_CURRENT_PAGE_POSITION, currentPosition)
    }

    override fun finishAfterTransition() {
        isReturning = true
        val data = Intent()
        data.putExtra(PhotoActivity.EXTRA_STARTING_ALBUM_POSITION, startingPosition)
        data.putExtra(PhotoActivity.EXTRA_CURRENT_ALBUM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    supportFinishAfterTransition()
                    return true
                }
                else -> {
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolBar() {
        setSupportActionBar(photo_toolbar)
        supportActionBar?.apply {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }
    }

    //    /**
//     * The [android.support.v4.view.PagerAdapter] that will provide
//     * fragments for each of the sections. We use a
//     * {@link FragmentPagerAdapter} derivative, which will keep every
//     * loaded fragment in memory. If this becomes too memory intensive, it
//     * may be best to switch to a
//     * [android.support.v4.app.FragmentStatePagerAdapter].
//     */
//    private var sectionsPagerAdapter: PhotoViewActivity.SectionsPagerAdapter? = null
//    private lateinit var images: ArrayList<Photo>
//    private var position: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_photo_view)
//        setSupportActionBar(app_toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
//
////        photoItem = intent.getParcelableExtra(StringConstants.PHOTO_KEY)
//        images = intent.getParcelableArrayListExtra<Photo>(StringConstants.PHOTOS_KEY)
//        position = intent.getIntExtra(StringConstants.PHOTO_POSITION, 0)
//
//        title = images[position].caption
//        GlideApp
//                .with(this)
//                .load(APIConstants.VCELNICE_BASE_URL + images[position].image)
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                        loading_content.visibility = View.GONE
//                        return false
//                    }
//                })
//                .override(images[position].width, images[position].height)
//                .fitCenter()
//                .transition(withCrossFade())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(image)
//    }
//
//    /**
//     * A [FragmentPagerAdapter] that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        override fun getItem(position: Int): Fragment {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            val image: Photo = images[position]
//            return PhotoViewActivity.PlaceholderFragment.newInstance(position, image.caption, image.image)
//        }
//
//        override fun getCount(): Int {
//            return images.size
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return images[position].caption
//        }
//    }
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    class PlaceholderFragment : Fragment() {
//
//        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                                  savedInstanceState: Bundle?): View? {
//            val rootView = inflater.inflate(R.layout.activity_photo_view, container, false)
////            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
//            return rootView
//        }
//
//        companion object {
//            /**
//             * The fragment argument representing the section number for this
//             * fragment.
//             */
//            private const val ARG_SECTION_NUMBER = "section_number"
//
//            /**
//             * Returns a new instance of this fragment for the given section
//             * number.
//             */
//            fun newInstance(sectionNumber: Int, name: String, url: String): PlaceholderFragment {
//                val fragment = PlaceholderFragment()
//                val args = Bundle()
//                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
//                fragment.arguments = args
//                return fragment
//            }
//        }
//    }
}
