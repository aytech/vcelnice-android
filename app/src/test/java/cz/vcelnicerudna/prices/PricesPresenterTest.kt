package cz.vcelnicerudna.prices

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.BaseTest
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
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
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class PricesPresenterTest : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: PricesContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: VcelniceAPI

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataStore: AppDatabase

    private lateinit var pricesPresenter: PricesPresenter

    private val dummyPrices: List<Price>
        get() {
            val prices = ArrayList<Price>()
            val firstPrice = Price()
            firstPrice.title = "First test price"
            val secondPrice = Price()
            secondPrice.title = "Second test price"

            prices.addAll(arrayOf(firstPrice, secondPrice))
            return prices
        }

    @Before
    fun setUp() {
        pricesPresenter = PricesPresenter(mockActivity, mockDataSource, mockLocalDataStore)
    }

    @Test
    fun testFetchPricesFromApi() {
        val prices = dummyPrices
        Mockito.doReturn(Observable.just(prices)).`when`(mockDataSource).getPrices()

        pricesPresenter.fetchPricesFromApi()

        Mockito.verify(mockDataSource).getPrices()
        Mockito.verify(mockActivity).showPrices(prices)
        Mockito.verify(mockActivity).onPricesLoaded()
        Mockito.verify(mockLocalDataStore.pricesDao()).insert(prices[0])
        Mockito.verify(mockLocalDataStore.pricesDao()).insert(prices[1])
    }

    @Test
    fun testFetchPricesFromApiError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("API fetch failed"))).`when`(mockDataSource).getPrices()

        pricesPresenter.fetchPricesFromApi()

        Mockito.verify(mockDataSource).getPrices()
        Mockito.verify(mockActivity).onNetworkError()
    }

    @Test
    fun testFetchPricesFromLocalDataStore() {
        val prices = dummyPrices
        given(mockLocalDataStore.pricesDao().getPrices()).willReturn(Single.just(prices))

        pricesPresenter.fetchPricesFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.pricesDao()).getPrices()
        Mockito.verify(mockActivity).showPrices(prices)
    }

    @Test
    fun testFetchPricesFromLocalDataStoreError() {
        given(mockLocalDataStore.pricesDao().getPrices()).willReturn(Single.error(Throwable("DB fetch error")))

        pricesPresenter.fetchPricesFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.pricesDao()).getPrices()
        Mockito.verify(mockActivity).showError()
    }

    @Test
    fun testPersistPrice() {
        val price = dummyPrices[0]
        pricesPresenter.persistPrice(price)

        Mockito.verify(mockLocalDataStore.pricesDao()).insert(price)
    }
}