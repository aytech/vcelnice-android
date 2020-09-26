package cz.vcelnicerudna.main

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.configuration.StringConstants.Companion.NEWS_KEY
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ActivityMainBinding
import cz.vcelnicerudna.loadHTML
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import cz.vcelnicerudna.news.NewsActivity
import cz.vcelnicerudna.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity(), MainContract.ViewInterface {

    private lateinit var mainPresenter: MainContract.PresenterInterface
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = MainViewModel(null, null, null)

        viewAdapter = NewsAdapter(listOf())
        recyclerView = home_news_recycler_view.apply { adapter = viewAdapter }

        mainPresenter = MainPresenter(this, RepositoryImpl(), appDatabase)

        more_news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            intent.putParcelableArrayListExtra(NEWS_KEY, ArrayList(mainPresenter.getNews()))
            startActivity(intent)
        }
        action_call_main.setOnClickListener { handleCallAction() }
        bottom_app_bar_main.setOnMenuItemClickListener { onNavigationItemSelected(it, null) }

        loadNews()
        loadHomeText()
    }

    private fun loadNews() {
        mainPresenter.fetchNewsFromApi()
    }

    private fun loadHomeText() {
        mainPresenter.fetchHomeTextFromApi()
    }

    override fun showHomeText(text: HomeText) {
        binding.main = MainViewModel(text.title, loadHTML(text.text), text.icon)
        main_image.visibility = VISIBLE
    }

    override fun showNews(news: List<News>) {
        if (news.isNotEmpty()) {
            viewAdapter.update(news)
            home_news_recycler_view.visibility = VISIBLE
            more_news.visibility = VISIBLE
        }
    }

    override fun onLoadingComplete() {
        loading_content.visibility = GONE
    }

    override fun onHomeTextNetworkError() {
        mainPresenter.fetchHomeTextFromLocalDataStore()
    }

    override fun onNewsNetworkError(error: Throwable) {
        Timber.d("Error loading news: $error")
    }

    override fun showError() {
        getIndefiniteSnack(main_view, action_call_main, R.string.network_error, R.string.reload) { loadHomeText() }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy()
    }
}
