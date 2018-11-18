package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R.layout.activity_main
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.HomeText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
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

    private fun loadHomeText() {
        loading_content.visibility = View.VISIBLE
        if (isConnectedToInternet()) {
            fetchTextFromAPI()
        } else {
            fetchHomeTextFromDB()
        }
    }

    private fun fetchTextFromAPI() {
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.getHomeText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: HomeText ->
                            onResultAction(result)
                            compositeDisposable.dispose()
                        }
                ) {
                    noResultAction()
                    compositeDisposable.dispose()
                }
        compositeDisposable.add(disposable)
    }

    private fun insertHomeTextToDB(homeText: HomeText) {
        val task = Runnable { appDatabase?.homeDao()?.insert(homeText) }
        appDatabaseWorkerThread.postTask(task)
    }

    private fun fetchHomeTextFromDB() {
        val task = Runnable {
            val homeText = appDatabase?.homeDao()?.getHomeText()
            uiHandler?.post {
                if (homeText == null) {
                    noResultAction()
                } else {
                    onResultAction(homeText)
                }
            }
        }
        appDatabaseWorkerThread.postTask(task)
    }

    private fun noResultAction() {
        loading_content.visibility = View.GONE
        val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.reload)) {
            snackbar.dismiss()
            loadHomeText()
        }
        snackbar.show()
    }

    private fun onResultAction(result: HomeText) {
        loading_content.visibility = View.GONE
        main_image.visibility = View.VISIBLE
        main_title.text = result.title
        main_text.text = loadHTML(result.text)
        Picasso
                .with(this)
                .load(APIConstants.VCELNICE_BASE_URL + result.icon)
                .placeholder(R.mipmap.ic_default_image)
                .into(main_image as ImageView)
        insertHomeTextToDB(result)
    }
}
