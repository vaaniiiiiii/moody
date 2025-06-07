package moody.network

import com.squareup.moshi.Moshi
import moody.model.Gambar
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import moody.model.OpStatus
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://dailylogger.sendiko.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MoodyApiService {
    @GET("daily.php")
    suspend fun getDaily(
        @Header("Authorization") userId: String
    ): List<Gambar>

    @Multipart
    @POST("daily.php")
    suspend fun postDaily(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("hari") hari: RequestBody,
        @Part("daily") daily: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}

object DailyApi{
    val service: MoodyApiService by lazy {
        retrofit.create(MoodyApiService::class.java)
    }
    fun getDailyUrl(imageId: String): String{
        return "${BASE_URL} imageId.php? id = $imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }