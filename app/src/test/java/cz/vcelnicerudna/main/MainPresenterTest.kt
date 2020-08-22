package cz.vcelnicerudna.main

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.BaseTest
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.HomeText
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
class MainPresenterTest : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: MainContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: VcelniceAPI

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataStore: AppDatabase

    lateinit var mainPresenter: MainPresenter

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
        mainPresenter = MainPresenter(viewInterface = mockActivity, vcelniceAPI = mockDataSource, localDataStore = mockLocalDataStore)
    }

    @Test
    fun testFetchHomeTextFromApi() {
        val dummyText = dummyHomeText
        Mockito.doReturn(Observable.just(dummyText)).`when`(mockDataSource).getHomeText()

        mainPresenter.fetchHomeTextFromApi()
        Mockito.verify(mockDataSource).getHomeText()
        Mockito.verify(mockActivity).showHomeText(dummyText)
        Mockito.verify(mockLocalDataStore.homeDao()).insert(dummyText)
    }

    @Test
    fun testFetchHomeTextFromLocalDataStore() {
        val dummyText = dummyHomeText
        given(mockLocalDataStore.homeDao().getHomeText()).willReturn(Single.just(dummyText))

        mainPresenter.fetchHomeTextFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.homeDao()).getHomeText()
        Mockito.verify(mockActivity).showHomeText(dummyText)
    }
}