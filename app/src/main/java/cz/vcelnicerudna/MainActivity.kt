package cz.vcelnicerudna

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R.layout.activity_main
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.HomeText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    private var appDatabase: AppDatabase? = null
    private var uiHandler: Handler? = null
    private lateinit var appDatabaseWorkerThread: AppDatabaseWorkerThread

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        super.actionBarToggleWithNavigation(this)

        appDatabaseWorkerThread = AppDatabaseWorkerThread("appDatabaseWorkerThread")
        appDatabaseWorkerThread.start()
        appDatabase = AppDatabase.getInstance(this)
        uiHandler = Handler()

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
        vcelniceAPI.getHomeText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: HomeText ->
                            loading_content.visibility = View.GONE
                            main_image.visibility = View.VISIBLE
                            main_title.text = result.title
                            main_text.text = Html.fromHtml(result.text)
                            Picasso
                                    .with(this)
                                    .load(APIConstants.VCELNICE_BASE_URL + result.icon)
                                    .placeholder(R.mipmap.ic_default_image)
                                    .into(main_image as ImageView)
                            // https://medium.com/mindorks/android-architecture-components-room-and-kotlin-f7b725c8d1d
                            insertHomeTextToDB(result)
                        }
                ) {
                    loading_content.visibility = View.GONE
                    val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction(getString(R.string.reload)) {
                        snackbar.dismiss()
                        loadHomeText()
                    }
                    snackbar.show()
                }
    }

    private fun insertHomeTextToDB(homeText: HomeText) {
        fetchHomeTextFromDB()
//        val task = Runnable { appDatabase?.homeDao()?.insert(homeText) }
//        appDatabaseWorkerThread.postTask(task)
        Log.d("MainActivity", "Inserting to DB: $homeText")
    }

    fun fetchHomeTextFromDB() {
        val task = Runnable {
            val homeText = appDatabase?.homeDao()?.getHomeText()
            uiHandler?.post {
                if (homeText == null) {
                    Log.d("MainActivity", "No data in cache!!!")
                } else {
                    Log.d("MainActivity", "Found data in cache!!! $homeText")
                }
            }
        }
        appDatabaseWorkerThread.postTask(task)
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        appDatabaseWorkerThread.quit()
        super.onDestroy()
    }
}
