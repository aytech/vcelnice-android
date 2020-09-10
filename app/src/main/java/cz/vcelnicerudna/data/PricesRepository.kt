package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response

interface PricesRepository {
    fun getReservationLocations(): Observable<List<Location>>
    fun postReservation(reservation: Reservation): Call<Response<Void>>
}