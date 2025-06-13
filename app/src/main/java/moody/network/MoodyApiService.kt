package moody.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import moody.model.ApiResponse
import moody.model.OpStatus
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://dailylogger.sendiko.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MoodyApiService {

    @GET("logs")
    @Headers("Accept: application/json")
    suspend fun getDaily(@Query("userId") userId: String): ApiResponse

    @Multipart
    @POST("logs")
    suspend fun postDaily(
        @Part("userId") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("mood") mood: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("logs/{id}")
    suspend fun deleteDaily(
        @Header("Authorization") userId: String,
        @Path("id") id: String
    ): OpStatus
}

object DailyApi {
    val service: MoodyApiService by lazy {
        retrofit.create(MoodyApiService::class.java)
    }
}

enum class ApiStatus {
    LOADING,
    SUCCESS,
    FAILED
}
