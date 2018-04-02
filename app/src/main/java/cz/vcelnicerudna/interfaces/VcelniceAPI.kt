package cz.vcelnicerudna.interfaces

import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface VcelniceAPI {

    @GET(APIConstants.HOME_TEXT_URL)
    fun getHomeText(): Observable<HomeText>

    @GET(APIConstants.NEWS_URL)
    fun getNews(): Observable<Array<News>>

    @GET(APIConstants.PRICES_URL)
    fun getPrices(): Observable<Array<Price>>

    @POST(APIConstants.EMAIL_POST_URL)
    @FormUrlEncoded
    fun postContactMessage(
            @Field("email") email: String,
            @Field("message") message: String): Observable<EmailResponse>

    @POST(APIConstants.RESERVE_POST_URL)
    @FormUrlEncoded
    fun reserve(
            @Field("amount") amount: Int,
            @Field("email") email: String,
            @Field("message") message: String,
            @Field("title") title: String): Observable<ReservationResponse>

    companion object {
        fun create(): VcelniceAPI {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(APIConstants.VCELNICE_BASE_URL)
                    .build()

            return retrofit.create(VcelniceAPI::class.java)
        }
    }
}
