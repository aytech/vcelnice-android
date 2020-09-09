package cz.vcelnicerudna.reserve

import com.nhaarman.mockitokotlin2.mock
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.PricesRepository
import cz.vcelnicerudna.models.Location
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ReservePresenterTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: ReserveContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: PricesRepository

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataSource: AppDatabase

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
//        val locations = dummyLocations
//        val mockedCall = Mockito.mock(Call::class.java)
//        Mockito.doReturn(mock(Call::class.java)).`when`(mockDataSource).getReservationLocations()
//        Mockito.doAnswer(Answer { invocation -> {
//            val callback = invocation.getArgument(0, Callback::class.java)
//            callback.onResponse(mockedCall, Response.success())
//        } }).`when`(mockedCall).enqueue(any(Callback::class.java))
//
//        reservePresenter.fetchLocationsFromApi()
//
//        Mockito.verify(mockDataSource).getReservationLocations()
//        Mockito.verify(mockActivity).showLocations(locations)
    }
}