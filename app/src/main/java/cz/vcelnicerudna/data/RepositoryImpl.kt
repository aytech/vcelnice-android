package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.data.net.RetrofitClient
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response

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

    override fun postReservation(reservation: Reservation): Call<Response<Void>> {
        return retrofitClient.postReservation(reservation)
    }
}