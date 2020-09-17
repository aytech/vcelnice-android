package cz.vcelnicerudna.data.net

import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.data.model.EmailMessage
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private val vcelniceApi: VcelniceApi

    init {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(
                        GsonConverterFactory.create())
                .baseUrl(APIConstants.VCELNICE_BASE_URL)
                .build()

        vcelniceApi = retrofit.create(VcelniceApi::class.java)
    }

    fun postReservation(reservation: Reservation): Call<Response<Void>> {
        return vcelniceApi.postReservation(
                amount = reservation.amount,
                email = reservation.email,
                message = reservation.message,
                title = reservation.title,
                location = reservation.location)
    }

    fun postContactMessage(message: EmailMessage): Call<Response<Void>> {
        return vcelniceApi.postContactMessage(email = message.email, message = message.message)
    }

    fun getReservationLocations(): Observable<List<Location>> {
        return vcelniceApi.getLocations()
    }

    fun getHomeText(): Observable<HomeText> {
        return vcelniceApi.getHomeText()
    }

    fun getNews(): Observable<List<News>> {
        return vcelniceApi.getNews()
    }
}