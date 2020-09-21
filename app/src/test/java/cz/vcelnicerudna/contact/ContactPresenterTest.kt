package cz.vcelnicerudna.contact

import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.EmailMessage
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContactPresenterTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: ContactContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: Repository

    private lateinit var presenter: ContactPresenter

    private val message: EmailMessage
        get() {
            return EmailMessage("test@vcelnice.cz", "test message")
        }

    @Before
    fun setUp() {
        presenter = ContactPresenter(mockActivity, mockDataSource)
    }

    @Test
    fun testPostContactMessage() {
        val localMessage = message
        Mockito.doReturn(Observable.just(localMessage))
                .`when`(mockDataSource).postContactMessage(localMessage)

        presenter.postContactMessage(localMessage)

        Mockito.verify(mockDataSource).postContactMessage(localMessage)
        Mockito.verify(mockActivity).onMessageSent()
        Mockito.verify(mockActivity).onMessageSendComplete()
    }

    @Test
    fun testPostContactMessageError() {
        val localMessage = message
        Mockito.doReturn(Observable.error<Throwable>(Throwable("Error fetching data")))
                .`when`(mockDataSource).postContactMessage(localMessage)

        presenter.postContactMessage(localMessage)

        Mockito.verify(mockDataSource).postContactMessage(localMessage)
        Mockito.verify(mockActivity).onMessageSentError()
    }
}