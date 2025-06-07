package moody.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://dailylogger.sendiko.my.id/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MoodyApiService {
    @GET("static-api.json")
    suspend fun getDaily(): String
}

object MoodyApi{
    val service: MoodyApiService by lazy{
        retrofit.create(MoodyApiService::class.java)
    }
}