package cz.vcelnicerudna.news

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.configuration.StringConstants.Companion.NEWS_KEY
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.models.News
import kotlinx.android.synthetic.main.activity_news.*
import java.util.ArrayList

class NewsActivity : BaseActivity(), NewsContract.ViewInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var newsPresenter: NewsContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(listOf())

        recyclerView = findViewById<RecyclerView>(R.id.news_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        newsPresenter = NewsPresenter(this, RepositoryImpl(), appDatabase)

        val bundledNews: ArrayList<News>? = intent.getParcelableArrayListExtra(NEWS_KEY)
        if (bundledNews == null) {
            fetchNewsFromApi()
        } else {
            showNews(bundledNews)
        }
    }

    private fun fetchNewsFromApi() {
        progress_bar.visibility = VISIBLE
        newsPresenter.fetchNewsFromAPI()
    }

    override fun showNews(news: List<News>) {
        progress_bar.visibility = GONE
        viewAdapter.update(news)
    }

    override fun onNetworkError() {
        newsPresenter.fetchNewsFromLocalDataStore()
    }

    override fun showError() {
        progress_bar.visibility = GONE
        getIndefiniteSnack(main_view_news, R.string.network_error, R.string.reload) { fetchNewsFromApi() }.show()
    }
}
