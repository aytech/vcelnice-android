package cz.vcelnicerudna.data.net

import cz.vcelnicerudna.configuration.APIConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VcelniceApi {
    @POST(APIConstants.RESERVE_POST_URL)
    @FormUrlEncoded
    fun postReservation(
            @Field("amount") amount: Int?,
            @Field("email") email: String?,
            @Field("message") message: String?,
            @Field("title") title: String,
            @Field("location") location: String): Call<Response<Void>>
}