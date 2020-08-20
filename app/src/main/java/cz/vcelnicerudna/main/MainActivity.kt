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
import cz.vcelnicerudna.isConnectedToInternet
import cz.vcelnicerudna.loadHTML
import cz.vcelnicerudna.models.HomeText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(), MainContract.ViewInterface {

    private lateinit var mainPresenter: MainContract.PresenterInterface
    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    private fun setupPresenter() {
        mainPresenter = MainPresenter(this)
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
        mainPresenter.fetchHomeTextFromApi()
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.getHomeText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { text: HomeText ->
                            onFetchSuccess(text)
                            insertHomeTextToDatabase(text)
                            compositeDisposable.dispose()
                        }
                ) {
                    onFetchError()
                    compositeDisposable.dispose()
                }
        compositeDisposable.add(disposable)
    }

    private fun insertHomeTextToDatabase(homeText: HomeText) {
        val task = Runnable { appDatabase?.homeDao()?.insert(homeText) }
        appDatabaseWorkerThread.postTask(task)
    }

    private fun fetchHomeTextFromDB() {
        val task = Runnable {
            val homeText = appDatabase?.homeDao()?.getHomeText()
            uiHandler?.post {
                if (homeText == null) {
                    onFetchError()
                } else {
                    onFetchSuccess(homeText)
                }
            }
        }
        appDatabaseWorkerThread.postTask(task)
    }

    private fun onFetchError() {
        loading_content.visibility = View.GONE
        val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.reload)) {
            snackbar.dismiss()
            loadHomeText()
        }
        snackbar.show()
    }

    private fun onFetchSuccess(result: HomeText) {
        loading_content.visibility = View.GONE
        main_image.visibility = View.VISIBLE
        main_title.text = result.title
        main_text.text = loadHTML(result.text)
        Picasso
                .with(this)
                .load(APIConstants.VCELNICE_BASE_URL + result.icon)
                .placeholder(R.mipmap.ic_default_image)
                .into(main_image as ImageView)
    }
}
