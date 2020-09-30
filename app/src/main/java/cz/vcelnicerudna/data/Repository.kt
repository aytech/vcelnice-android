package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.*
import io.reactivex.Observable

interface Repository {
    fun getHomeText(): Observable<HomeText>
    fun getNews(): Observable<List<News>>
    fun getPhotos(): Observable<List<Photo>>
    fun getPrices(): Observable<List<Price>>
    fun getReservationLocations(): Observable<List<Location>>
    fun postReservation(reservation: Reservation): Observable<Reservation>
    fun postContactMessage(message: EmailMessage): Observable<EmailMessage>
}