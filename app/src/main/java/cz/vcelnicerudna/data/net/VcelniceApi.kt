package cz.vcelnicerudna.data.net

import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.Location
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface VcelniceApi {
    @GET(APIConstants.LOCATIONS_URL)
    fun getLocations(): Call<List<Location>>

    @POST(APIConstants.RESERVE_POST_URL)
    @FormUrlEncoded
    fun postReservation(
            @Field("amount") amount: Int?,
            @Field("email") email: String?,
            @Field("message") message: String?,
            @Field("title") title: String?,
            @Field("location") location: Location?): Call<Response<Void>>
}