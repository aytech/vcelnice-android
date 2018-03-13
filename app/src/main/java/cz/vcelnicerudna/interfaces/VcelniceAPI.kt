package cz.vcelnicerudna.interfaces

import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface VcelniceAPI {

    @GET(APIConstants.HOME_TEXT_URL)
    fun getHomeText(): Observable<HomeText>

    @GET(APIConstants.NEWS_URL)
    fun getNews(): Observable<Array<News>>

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
