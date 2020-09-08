package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.data.net.RetrofitClient
import cz.vcelnicerudna.models.Location
import retrofit2.Call
import retrofit2.Response

class PricesRepositoryImpl : PricesRepository {

    private val retrofitClient = RetrofitClient()

    override fun getReservationLocations(): Call<List<Location>> {
        return retrofitClient.getReservationLocations()
    }

    override fun postReservation(reservation: Reservation): Call<Response<Void>> {
        return retrofitClient.postReservation(reservation)
    }
}