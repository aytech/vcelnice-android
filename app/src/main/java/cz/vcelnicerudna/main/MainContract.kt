package cz.vcelnicerudna.main

import cz.vcelnicerudna.data.model.HomeText
import cz.vcelnicerudna.data.model.News

class MainContract {
    interface PresenterInterface {
        fun fetchHomeTextFromApi()
        fun fetchNewsFromApi()
        fun fetchHomeTextFromLocalDataStore()
        fun fetchNewsFromLocalDataStore()
        fun getNews(): List<News>
        fun persistHomeText(text: HomeText)
        fun persistNews(news: News)
        fun onDestroy()
    }

    interface ViewInterface {
        fun onLoadingComplete()
        fun onHomeTextNetworkError()
        fun onNewsNetworkError(error: Throwable)
        fun showError()
        fun showHomeText(text: HomeText)
        fun showNews(news: List<News>)
    }
}