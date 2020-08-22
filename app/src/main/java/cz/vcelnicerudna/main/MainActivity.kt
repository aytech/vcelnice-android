package cz.vcelnicerudna.main

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.R.layout.activity_main
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.loadHTML
import cz.vcelnicerudna.models.HomeText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(), MainContract.ViewInterface {

    private lateinit var mainPresenter: MainContract.PresenterInterface

    private fun setupPresenter() {
        mainPresenter = MainPresenter(this, VcelniceAPI.create(), appDatabase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        super.actionBarToggleWithNavigation(this)
        setupPresenter()
        loadHomeText()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadHomeText() {
        loading_content.visibility = View.VISIBLE
        mainPresenter.fetchHomeTextFromApi()
    }

    override fun showHomeText(text: HomeText) {
        loading_content.visibility = View.GONE
        main_title.text = text.title
        main_text.text = loadHTML(text.text)
        main_image.visibility = View.VISIBLE
        Picasso
                .get()
                .load(APIConstants.VCELNICE_BASE_URL + text.icon)
                .placeholder(R.mipmap.ic_default_image)
                .into(main_image as ImageView)
    }

    override fun onNetworkError() {
        mainPresenter.fetchHomeTextFromLocalDataStore()
    }

    override fun showError() {
        loading_content.visibility = View.GONE
        val snackBar = getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.reload)) {
            snackBar.dismiss()
            loadHomeText()
        }
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy()
    }
}
