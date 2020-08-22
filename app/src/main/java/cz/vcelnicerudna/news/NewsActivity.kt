package cz.vcelnicerudna.news

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.News
import kotlinx.android.synthetic.main.content_news.*

class NewsActivity : BaseActivity(), NewsContract.ViewInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var newsPresenter: NewsContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        super.actionBarToggleWithNavigation(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(this, listOf())

        recyclerView = findViewById<RecyclerView>(R.id.news_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        newsPresenter = NewsPresenter(this, VcelniceAPI.create(), appDatabase)
        loadNews()
    }

    private fun loadNews() {
        loading_content.visibility = View.VISIBLE
        newsPresenter.fetchNewsFromAPI()
    }

    override fun showNews(news: List<News>) {
        loading_content.visibility = View.GONE
        viewAdapter.loadNewData(news)
    }

    override fun onNetworkError() {
        newsPresenter.fetchNewsFromLocalDataStore()
    }

    override fun showError() {
        loading_content.visibility = View.GONE
        val snackBar = getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.reload)) {
            snackBar.dismiss()
            loadNews()
        }
        snackBar.show()
    }
}
