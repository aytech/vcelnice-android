package cz.vcelnicerudna.reserve

import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.PricesRepository
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReservePresenterTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: ReserveContract.ViewInterface

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataSource: AppDatabase

    @Mock
    private lateinit var mockDataSource: PricesRepository

    private lateinit var reservePresenter: ReservePresenter

    private val dummyLocations: List<Location>
        get() {
            val locations = ArrayList<Location>()
            val location = Location()
            location.address = "Test address"
            locations.addAll(arrayOf(location))
            return locations
        }

    @Before
    fun setUp() {
        reservePresenter = ReservePresenter(mockActivity, mockDataSource, mockLocalDataSource)
    }

    @Test
    fun testFetchLocationsFromApi() {
        val locations = dummyLocations
        Mockito.doReturn(Observable.just(locations)).`when`(mockDataSource).getReservationLocations()

        reservePresenter.fetchLocationsFromApi()

        Mockito.verify(mockDataSource).getReservationLocations()
        Mockito.verify(mockActivity).showLocations(locations)
    }
}