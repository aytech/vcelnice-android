package cz.vcelnicerudna.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
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
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ContentMainBinding
import cz.vcelnicerudna.loadHTML
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import cz.vcelnicerudna.news.NewsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(), MainContract.ViewInterface {

    private lateinit var mainPresenter: MainContract.PresenterInterface
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        val binding = DataBindingUtil.setContentView<ContentMainBinding>(this, R.layout.content_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        //setContentView(activity_main)
//        super.actionBarToggleWithNavigation(this, binding.mainToolbar as Toolbar?)
        //setSupportActionBar(binding.mainToolbar as Toolbar?)
        setBottomNavigationMenu(bottom_navigation_menu)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(this, listOf())
        recyclerView = home_news_recycler_view.apply {
            //setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        mainPresenter = MainPresenter(this, RepositoryImpl(), appDatabase)

        loadNews()
        loadHomeText()

        more_news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadNews() {
        loading_content.visibility = VISIBLE
        mainPresenter.fetchNewsFromApi()
    }

    private fun loadHomeText() {
        loading_content.visibility = VISIBLE
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
        loading_content.visibility = GONE
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

    override fun onNewsNetworkError() {
        TODO("Not yet implemented")
    }

    override fun showError() {
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
