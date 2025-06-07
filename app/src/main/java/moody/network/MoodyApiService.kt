package moody.network

import com.squareup.moshi.Moshi
import moody.model.Gambar
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/indraazimi/mobpro1-compose/static-api/static-api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MoodyApiService {
    @GET("static-api.json")
    suspend fun getDaily(): List<Gambar>
}

object DailyApi{
    val service: MoodyApiService by lazy {
        retrofit.create(MoodyApiService::class.java)
    }
    fun getDailyUrl(imageId: String): String{
        return "$BASE_URL$imageId.jpg"
    }
}