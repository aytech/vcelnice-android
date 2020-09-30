package cz.vcelnicerudna.data.net

import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.data.model.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface VcelniceApi {
    @GET(APIConstants.LOCATIONS_URL)
    fun getLocations(): Observable<List<Location>>

    @GET(APIConstants.PHOTO_URL)
    fun getPhotos(): Observable<List<Photo>>

    @GET(APIConstants.PRICES_URL)
    fun getPrices(): Observable<List<Price>>

    @POST(APIConstants.RESERVE_POST_URL)
    @FormUrlEncoded
    fun postReservation(
            @Field("amount") amount: Int?,
            @Field("email") email: String?,
            @Field("message") message: String?,
            @Field("title") title: String?,
            @Field("location") location: String?): Observable<Reservation>

    @POST(APIConstants.EMAIL_POST_URL)
    @FormUrlEncoded
    fun postContactMessage(
            @Field("email") email: String?,
            @Field("message") message: String?): Observable<EmailMessage>

    @GET(APIConstants.NEWS_URL)
    fun getNews(): Observable<List<News>>

    @GET(APIConstants.HOME_TEXT_URL)
    fun getHomeText(): Observable<HomeText>
}