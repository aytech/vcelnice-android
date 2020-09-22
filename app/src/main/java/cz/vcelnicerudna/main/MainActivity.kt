package cz.vcelnicerudna.main

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants.Companion.NEWS_KEY
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ActivityMainBinding
import cz.vcelnicerudna.loadHTML
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import cz.vcelnicerudna.news.NewsActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity(), MainContract.ViewInterface {

    private lateinit var mainPresenter: MainContract.PresenterInterface
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(this, listOf())
        recyclerView = home_news_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

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
        main_title.text = text.title
        main_text.text = loadHTML(text.text)
        main_image.visibility = VISIBLE
        Picasso
                .get()
                .load(APIConstants.VCELNICE_BASE_URL + text.icon)
                .placeholder(R.mipmap.ic_default_image)
                .into(main_image as ImageView)
    }

    override fun showNews(news: List<News>) {
        if (news.isNotEmpty()) {
            viewAdapter.loadNewData(news)
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
        val snackBar = getIndefiniteSnack(main_view, bottom_app_bar_main, R.string.network_error)
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
