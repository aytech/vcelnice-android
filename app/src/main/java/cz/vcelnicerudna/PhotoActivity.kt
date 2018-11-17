package cz.vcelnicerudna

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewTreeObserver
import cz.vcelnicerudna.adapters.PhotoAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Photo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity(), PhotoAdapter.OnItemClickListener {

    private var reenterState: Bundle? = null
    private var photos: ArrayList<Photo> = ArrayList()
    private var photoAdapter: PhotoAdapter

    companion object {
        const val EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position"
        const val EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position"
    }

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    init {
        photoAdapter = PhotoAdapter(photos, this)
    }

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
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
        super.actionBarToggleWithNavigation(this)
        ActivityCompat.setExitSharedElementCallback(this, exitElementCallback)

        photo_collection.setHasFixedSize(true)
        photo_collection.layoutManager = GridLayoutManager(this, 3)
        photo_collection.adapter = photoAdapter
        photo_collection.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        photo_collection.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        loadPhoto()
    }

    override fun onClick(item: Photo, view: View) {
        val intent = Intent(this, PhotoViewActivity::class.java)
        intent.putParcelableArrayListExtra(StringConstants.PHOTOS_KEY, photos)
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

    private fun loadPhoto() {
        loading_content.visibility = View.VISIBLE
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.getPhoto()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loading_content.visibility = View.GONE
                            if (result.isEmpty()) {
                                no_content.visibility = View.VISIBLE
                            } else {
                                no_content.visibility = View.GONE
                                photos = result.toCollection(ArrayList())
                                photoAdapter.loadData(photos)
                            }
                            compositeDisposable.dispose()
                        }
                ) {
                    loading_content.visibility = View.GONE
                    val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction(getString(R.string.reload)) {
                        snackbar.dismiss()
                        loadPhoto()
                    }
                    snackbar.show()
                    compositeDisposable.dispose()
                }
        compositeDisposable.add(disposable)
    }
}
