package cz.vcelnicerudna

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
        override fun onMapSharedElements(names: MutableList<String?>, sharedElements: MutableMap<String?, View>) {
            if (isReturning) {
                val sharedElement = photoPagerAdapter?.getView(currentPosition) as View

                if (startingPosition != currentPosition) {
                    names.clear()
                    names.add(ViewCompat.getTransitionName(sharedElement))

                    sharedElements.clear()
                    sharedElements[ViewCompat.getTransitionName(sharedElement)] = sharedElement
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
        supportActionBar?.apply {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }
    }
}
