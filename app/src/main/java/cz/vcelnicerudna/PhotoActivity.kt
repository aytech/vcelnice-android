package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.fragments.PhotoRecyclerViewFragment
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        super.actionBarToggleWithNavigation(this)
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
                                val fragment = PhotoRecyclerViewFragment()
                                val bundle = Bundle()
                                bundle.putParcelableArrayList(StringConstants.PHOTOS_KEY, result.toCollection(ArrayList()))
                                fragment.arguments = bundle
                                supportFragmentManager
                                        .beginTransaction()
                                        .add(R.id.content, fragment)
                                        .commit()
                            }
                        }
                ) {
                    loading_content.visibility = View.GONE
                    val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction(getString(R.string.reload)) {
                        snackbar.dismiss()
                        loadPhoto()
                    }
                    snackbar.show()
                }
    }
}
