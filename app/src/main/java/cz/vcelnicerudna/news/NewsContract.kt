package cz.vcelnicerudna.news

import cz.vcelnicerudna.data.model.News

class NewsContract {
    interface PresenterInterface {
        fun fetchNewsFromAPI()
        fun fetchNewsFromLocalDataStore()
        fun persistNews(news: News)
        fun onDestroy()
    }
    interface ViewInterface {
        fun showNews(news: List<News>)
        fun onNetworkError()
        fun showError()
    }
}