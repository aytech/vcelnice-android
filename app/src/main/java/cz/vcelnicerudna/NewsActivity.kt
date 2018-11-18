package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.News
import cz.vcelnicerudna.models.NewsData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_news.*

class NewsActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        super.actionBarToggleWithNavigation(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(this, arrayOf())

        recyclerView = findViewById<RecyclerView>(R.id.news_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        loadNews()
    }

    private fun loadNews() {
        loading_content.visibility = View.VISIBLE
        if (isConnectedToInternet()) {
            fetchNewsFromAPI()
        } else {
            fetchNewsFromDatabase()
        }
    }

    private fun fetchNewsFromAPI() {
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { news: Array<News> ->
                            onFetchSuccess(news)
                            insertNewsToDatabase(news)
                            compositeDisposable.dispose()
                        }
                ) {
                    onFetchError()
                    compositeDisposable.dispose()
                }
        compositeDisposable.add(disposable)
    }

    private fun fetchNewsFromDatabase() {
        val task = Runnable {
            val news = appDatabase?.newsDao()?.getNews()
            uiHandler?.post {
                if (news == null) {
                    onFetchError()
                } else {
                    onFetchSuccess(news.data)
                }
            }
        }
        appDatabaseWorkerThread.postTask(task)
    }

    private fun onFetchSuccess(result: Array<News>) {
        loading_content.visibility = View.GONE
        viewAdapter.loadNewData(result)
    }

    private fun onFetchError() {
        loading_content.visibility = View.GONE
        val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.reload)) {
            snackbar.dismiss()
            loadNews()
        }
        snackbar.show()
    }

    private fun insertNewsToDatabase(news: Array<News>) {
        val newsData = NewsData()
        newsData.data = news
        appDatabaseWorkerThread.postTask(Runnable { appDatabase?.newsDao()?.insert(newsData) })
    }
}
