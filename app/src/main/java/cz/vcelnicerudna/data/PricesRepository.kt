package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location
import retrofit2.Call
import retrofit2.Response

interface PricesRepository {
    fun getReservationLocations(): Call<List<Location>>
    fun postReservation(reservation: Reservation): Call<Response<Void>>
}