package cz.vcelnicerudna.photo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.view.ViewTreeObserver
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.PhotoAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Photo
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity(), PhotoAdapter.OnItemClickListener, PhotoContract.ViewInterface {

    private var reenterState: Bundle? = null
    private var photoAdapter: PhotoAdapter = PhotoAdapter(listOf(), this)
    private lateinit var photoPresenter: PhotoPresenter

    companion object {
        const val EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position"
        const val EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position"
    }

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            val photos = photoAdapter.getPhotos()
            if (reenterState != null) {
                val startingPosition = reenterState!!.getInt(EXTRA_STARTING_ALBUM_POSITION)
                val currentPosition = reenterState!!.getInt(EXTRA_CURRENT_ALBUM_POSITION)
                if (startingPosition != currentPosition) {
                    // Current element has changed, need to override previous exit transitions
                    val newTransitionName = Photo.transitionName(photos[currentPosition].id)
                    val newSharedElement = photo_collection.findViewWithTag<View>(newTransitionName)
                    if (newSharedElement != null) {
                        names?.clear()
                        names?.add(newTransitionName)
                        sharedElements?.clear()
                        sharedElements?.put(newTransitionName, newSharedElement)
                    }
                }
                reenterState = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        photoPresenter = PhotoPresenter(this, VcelniceAPI.create(), appDatabase)
        ActivityCompat.setExitSharedElementCallback(this, exitElementCallback)

        photo_collection.setHasFixedSize(true)
        photo_collection.layoutManager = GridLayoutManager(this, 3)
        photo_collection.adapter = photoAdapter
        photo_collection.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        photo_collection.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        bottom_app_bar_photo.setNavigationOnClickListener { navigateHome() }
        bottom_app_bar_photo.setOnMenuItemClickListener { onNavigationItemSelected(it, R.id.photo_page) }

        loadPhotos()
    }

    override fun onClick(item: Photo, view: View) {
        val intent = Intent(this, PhotoViewActivity::class.java)
        intent.putParcelableArrayListExtra(StringConstants.PHOTOS_KEY, ArrayList(photoAdapter.getPhotos()))
        intent.putExtra(PhotoViewActivity.ITEM_ID, item.id)

        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val pair = Pair.create(view, ViewCompat.getTransitionName(view))
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair).toBundle()
        }
        // Open detail activity with shared element transition
        startActivity(intent, bundle)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        reenterState = Bundle(data?.extras)
        reenterState?.let {
            val startingPosition = it.getInt(EXTRA_STARTING_ALBUM_POSITION)
            val currentPosition = it.getInt(EXTRA_CURRENT_ALBUM_POSITION)
            if (startingPosition != currentPosition) photo_collection.scrollToPosition(currentPosition)
            ActivityCompat.postponeEnterTransition(this)

            photo_collection.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    photo_collection.viewTreeObserver.removeOnPreDrawListener(this)
                    ActivityCompat.startPostponedEnterTransition(this@PhotoActivity)
                    return true
                }
            })
        }
    }

    private fun loadPhotos() {
        loading_content.visibility = View.VISIBLE
        photoPresenter.fetchPhotosFromAPI()
    }

    override fun showPhotos(photos: List<Photo>) {
        loading_content.visibility = View.GONE
        if (photos.isEmpty()) {
            no_content.visibility = View.VISIBLE
        } else {
            no_content.visibility = View.GONE
            photoAdapter.loadData(photos)
        }
    }

    override fun onNetworkError() {
        photoPresenter.fetchPhotosFromLocalDataStore()
    }

    override fun showError() {
        loading_content.visibility = View.GONE
        val snackBar = getThemedSnackBar(main_photo_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.reload)) {
            snackBar.dismiss()
            loadPhotos()
        }
        snackBar.show()
    }
}
