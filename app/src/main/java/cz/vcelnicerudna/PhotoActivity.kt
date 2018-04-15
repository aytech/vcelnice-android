package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.adapters.PhotoAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PhotoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        super.actionBarToggleWithNavigation(this)

        viewManager = GridLayoutManager(this, 3)
        viewAdapter = PhotoAdapter(this, arrayOf())

        recyclerView = findViewById<RecyclerView>(R.id.photo_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
        loadPhoto()
    }

    private fun loadPhoto() {
        loading_content.visibility = View.VISIBLE
        vcelniceAPI.getPhoto()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loading_content.visibility = View.GONE
                            if (result.isEmpty()) {
                                no_content.visibility = View.VISIBLE
                            } else {
                                no_content.visibility = View.GONE
                                viewAdapter.loadNewData(result)
                            }
                        },
                        {
                            loading_content.visibility = View.GONE
                            val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                            snackbar.setAction(getString(R.string.reload), {
                                snackbar.dismiss()
                                loadPhoto()
                            })
                            snackbar.show()
                        }
                )
    }
}