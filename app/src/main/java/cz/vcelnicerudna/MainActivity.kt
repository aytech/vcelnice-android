package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.actionBarToggleWithNavigation(this)
        loadHomeText()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun loadHomeText() {
        vcelniceAPI.getHomeText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loading_content.visibility = View.GONE
                            main_image.visibility = View.VISIBLE
                            main_title.text = result.title
                            main_text.text = Html.fromHtml(result.text)
                            GlideApp
                                    .with(this)
                                    .load(APIConstants.VCELNICE_BASE_URL + result.icon)
                                    .placeholder(R.mipmap.ic_default_image)
                                    .fitCenter()
                                    .into(main_image)
                        },
                        { _ ->
                            loading_content.visibility = View.GONE
                            val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                            snackbar.setAction(getString(R.string.close), { snackbar.dismiss() })
                            snackbar.show()
                        }
                )
    }
}
