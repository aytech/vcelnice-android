package cz.vcelnicerudna.main

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.BaseTest
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: MainContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: Repository

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataStore: AppDatabase

    private lateinit var mainPresenter: MainPresenter

    private val dummyNews: List<News>
        get() {
            val news1 = News()
            val news2 = News()
            val news3 = News()
            val news4 = News()

            news1.title = "News 1"
            news2.title = "News 2"
            news3.title = "News 3"
            news4.title = "News 4"

            return listOf(news1, news2, news3, news4)
        }
    private val dummyHomeText: HomeText
        get() {
            val text = HomeText()
            text.title = "Test Title"
            text.icon = "/dummy/path"
            text.text = "Test Text"
            return text
        }

    @Before
    fun setUp() {
        mainPresenter = MainPresenter(mockActivity, mockDataSource, mockLocalDataStore)
    }

    @Test
    fun testFetchHomeTextFromApi() {
        val dummyText = dummyHomeText
        Mockito.doReturn(Observable.just(dummyText)).`when`(mockDataSource).getHomeText()

        mainPresenter.fetchHomeTextFromApi()

        Mockito.verify(mockDataSource).getHomeText()
        Mockito.verify(mockActivity).showHomeText(dummyText)
        Mockito.verify(mockActivity).onLoadingComplete()
        Mockito.verify(mockLocalDataStore.homeDao()).insert(dummyText)
    }

    @Test
    fun testFetchHomeTextFromApiError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("Error fetching home text"))).`when`(mockDataSource).getHomeText()

        mainPresenter.fetchHomeTextFromApi()

        Mockito.verify(mockActivity).onHomeTextNetworkError()
    }

    @Test
    fun testFetchNewsFromApi() {
        val news = dummyNews
        Mockito.doReturn(Observable.just(news)).`when`(mockDataSource).getNews()

        mainPresenter.fetchNewsFromApi()

        Mockito.verify(mockDataSource).getNews()
        Mockito.verify(mockActivity).showNews(news.slice(0..2))
        Mockito.verify(mockActivity).onLoadingComplete()
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news = news[0])
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news = news[1])
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news = news[2])
        Mockito.verify(mockLocalDataStore.newsDao()).insert(news = news[3])
        Assert.assertEquals(news, mainPresenter.getNews())
    }

    @Test
    fun testFetchNewsFromApiError() {
        val error = Throwable("Error fetching news")
        Mockito.doReturn(Observable.error<Throwable>(error)).`when`(mockDataSource).getNews()

        mainPresenter.fetchNewsFromApi()

        Mockito.verify(mockActivity).onNewsNetworkError(error)
    }

    @Test
    fun testFetchHomeTextFromLocalDataStore() {
        val dummyText = dummyHomeText
        given(mockLocalDataStore.homeDao().getHomeText()).willReturn(Single.just(dummyText))

        mainPresenter.fetchHomeTextFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.homeDao()).getHomeText()
        Mockito.verify(mockActivity).showHomeText(dummyText)
    }

    @Test
    fun testFetchHomeTextFromLocalDataStoreError() {
        given(mockLocalDataStore.homeDao().getHomeText()).willReturn(Single.error(Throwable("Error fetching text from local data store")))

        mainPresenter.fetchHomeTextFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.homeDao()).getHomeText()
        Mockito.verify(mockActivity).showError()
    }

    @Test
    fun testFetchNewsFromLocalDataStore() {
        val news = dummyNews
        given(mockLocalDataStore.newsDao().getNews()).willReturn(Single.just(news))

        mainPresenter.fetchNewsFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.newsDao()).getNews()
        Mockito.verify(mockActivity).showNews(news.slice(0..2))
        Assert.assertEquals(news, mainPresenter.getNews())
    }

    @Test
    fun testFetchNewsFromLocalDataStoreError() {
        given(mockLocalDataStore.newsDao().getNews()).willReturn(Single.error(Throwable("Error fetching news from local data store")))

        mainPresenter.fetchNewsFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.newsDao()).getNews()
        Mockito.verify(mockActivity).showError()
    }

    @Test
    fun testPersistHomeText() {
        val text = dummyHomeText

        mainPresenter.persistHomeText(text)

        Mockito.verify(mockLocalDataStore.homeDao()).insert(text)
    }

    @Test
    fun testPersistNews() {
        val news = dummyNews

        mainPresenter.persistNews(news = news[0])

        Mockito.verify(mockLocalDataStore.newsDao()).insert(news = news[0])
    }
}