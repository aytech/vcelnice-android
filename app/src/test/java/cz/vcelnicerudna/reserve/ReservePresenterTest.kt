package cz.vcelnicerudna.reserve

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location
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
class ReservePresenterTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: ReserveContract.ViewInterface

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataSource: AppDatabase

    @Mock
    private lateinit var mockDataSource: Repository

    private lateinit var reservePresenter: ReservePresenter

    private val dummyLocations: List<Location>
        get() {
            return listOf(Location.default())
        }
    private val reservationMock: Reservation
        get() {
            return Reservation("Test reservation", 1, "Test location", "test@test.com", "Test message")
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
        Mockito.verify(mockActivity).onLocationsFetchComplete()
    }

    @Test
    fun testFetchLocationsFromApiError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("API call failed!"))).`when`(mockDataSource).getReservationLocations()

        reservePresenter.fetchLocationsFromApi()

        Mockito.verify(mockDataSource).getReservationLocations()
        Mockito.verify(mockActivity).onNetworkError()
    }

    @Test
    fun testFetchLocationsFromLocalDataStore() {
        val locations = dummyLocations
        given(mockLocalDataSource.locationsDao().getLocations()).willReturn(Single.just(locations))

        reservePresenter.fetchLocationsFromLocalDataStore()

        Mockito.verify(mockLocalDataSource.locationsDao()).getLocations()
        Mockito.verify(mockActivity).showLocations(locations)
    }

    @Test
    fun testFetchLocationsFromLocalDataStoreError() {
        val error = Throwable("Fetch from local DB failed")
        given(mockLocalDataSource.locationsDao().getLocations()).willReturn(Single.error(error))

        reservePresenter.fetchLocationsFromLocalDataStore()

        Mockito.verify(mockLocalDataSource.locationsDao()).getLocations()
        Mockito.verify(mockActivity).showDefaultLocation()
    }

    @Test
    fun testPersistLocation() {
        val location = Location.default()
        reservePresenter.persistLocation(location)
        Mockito.verify(mockLocalDataSource.locationsDao()).insert(location)
    }

    @Test
    fun testPostReservation() {
        val reservation = reservationMock
        Mockito.doReturn(Observable.just(reservation))
                .`when`(mockDataSource).postReservation(reservation)

        reservePresenter.postReservation(reservation)

        Mockito.verify(mockDataSource).postReservation(reservation)
        Mockito.verify(mockActivity).onSuccessPostReservation()
        Mockito.verify(mockActivity).onCompletePostReservation()
    }

    @Test
    fun testPostReservationError() {
        val reservation = reservationMock
        val error = Throwable("Error posting reservation")
        Mockito.doReturn(Observable.error<Throwable>(error)).`when`(mockDataSource).postReservation(reservation)

        reservePresenter.postReservation(reservation)

        Mockito.verify(mockDataSource).postReservation(reservation)
        Mockito.verify(mockActivity).onFailPostReservation(error)
    }
}