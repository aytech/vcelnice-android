package cz.vcelnicerudna.data

import android.util.Log
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.data.net.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PricesRepositoryImpl : PricesRepository {

    private val retrofitClient = RetrofitClient()

    override fun postReservation(reservation: Reservation) {
        retrofitClient.postReservation(reservation)
                .enqueue(object : Callback<Response<Void>> {
                    override fun onResponse(call: Call<Response<Void>>, response: Response<Response<Void>>) {
                        Log.d(PricesRepositoryImpl::class.simpleName, "Post Success: Call: $call, Response: $response")
                    }

                    override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                        Log.d(PricesRepositoryImpl::class.simpleName, "Post failure: Call: $call, error: $t")
                    }
                })
    }
}