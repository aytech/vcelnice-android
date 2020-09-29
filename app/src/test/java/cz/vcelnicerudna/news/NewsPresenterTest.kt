package cz.vcelnicerudna.news

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.BaseTest
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsPresenterTest : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: NewsContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: Repository

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataStore : AppDatabase

    private lateinit var newsPresenter: NewsPresenter

    private val dummyNews: List<News>
        get() {
            val news = ArrayList<News>()
            val firstNews = News()
            firstNews.title = "First news title"
            firstNews.text = "First news text"
            firstNews.icon = "/dummy/icon"
            val secondNews = News()
            secondNews.title = "Second news title"
            secondNews.text = "Second news text"
            secondNews.icon = "/dummy/icon"

            news.add(firstNews)
            news.add(secondNews)
            return news
        }

    @Before
    fun setUp() {
        newsPresenter = NewsPresenter(mockActivity, mockDataSource, mockLocalDataStore)
    }

    @Test
    fun testFetchNewsFromApi() {
        val news = dummyNews
        Mockito.doReturn(Observable.just(news)).`when`(mockDataSource).getNews()

        newsPresenter.fetchNewsFromAPI()

        Mockito.verify(mockDataSource).getNews()
        Mockito.verify(mockActivity).showNews(news)
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news[0])
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news[1])
    }

    @Test
    fun testFetchNewsFromApiError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("API fetch failed"))).`when`(mockDataSource).getNews()

        newsPresenter.fetchNewsFromAPI()

        Mockito.verify(mockDataSource).getNews()
        Mockito.verify(mockActivity).onNetworkError()
    }

    @Test
    fun testFetchNewsFromLocalDataStore() {
        val news = dummyNews
        given(mockLocalDataStore.newsDao().getNews()).willReturn(Single.just(news))

        newsPresenter.fetchNewsFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.newsDao()).getNews()
        Mockito.verify(mockActivity).showNews(news)
    }

    @Test
    fun testPersistNews() {
        val news = dummyNews[0]
        newsPresenter.persistNews(news)

        Mockito.verify(mockLocalDataStore.newsDao()).insert(news)
    }
}