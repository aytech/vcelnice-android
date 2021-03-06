package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.EmailMessage
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.News
import io.reactivex.Observable

interface Repository {
    fun getHomeText(): Observable<HomeText>
    fun getNews(): Observable<List<News>>
    fun getReservationLocations(): Observable<List<Location>>
    fun postReservation(reservation: Reservation): Observable<Reservation>
    fun postContactMessage(message: EmailMessage): Observable<EmailMessage>
}