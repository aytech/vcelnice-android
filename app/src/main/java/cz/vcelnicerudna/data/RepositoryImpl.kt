package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.EmailMessage
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.data.net.RetrofitClient
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.News
import io.reactivex.Observable

class RepositoryImpl : Repository {

    private val retrofitClient = RetrofitClient()

    override fun getHomeText(): Observable<HomeText> {
        return retrofitClient.getHomeText()
    }

    override fun getNews(): Observable<List<News>> {
        return retrofitClient.getNews()
    }

    override fun getReservationLocations(): Observable<List<Location>> {
        return retrofitClient.getReservationLocations()
    }

    override fun postReservation(reservation: Reservation): Observable<Reservation> {
        return retrofitClient.postReservation(reservation)
    }

    override fun postContactMessage(message: EmailMessage): Observable<EmailMessage> {
        return retrofitClient.postContactMessage(message)
    }
}