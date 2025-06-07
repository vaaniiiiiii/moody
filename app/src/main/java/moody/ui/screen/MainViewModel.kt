package moody.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moody.database.HarianDao
import moody.model.Gambar
import moody.model.Harian
import moody.network.ApiStatus
import moody.network.DailyApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel(private val dao: HarianDao) : ViewModel() {

    var dataDaily = mutableStateOf(emptyList<Gambar>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var erroMassage = mutableStateOf<String?>(null)
        private set

    init {
        retrieveData()
    }

    fun retrieveData(){
        viewModelScope.launch (Dispatchers.IO){
            status.value = ApiStatus.LOADING
            try {
                dataDaily.value = DailyApi.service.getDaily()
                status.value = ApiStatus.SUCCESS
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, judul: String, hari: String, daily: String, bitmap: Bitmap){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = DailyApi.service.postDaily(
                    userId,
                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    hari.toRequestBody("text/plain".toMediaTypeOrNull()),
                    daily.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                if (result.status == "success")
                    retrieveData()
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part{
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)

        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage() {erroMassage.value = null}

    val data: StateFlow<List<Harian>> = dao.getHarian()

        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    fun undoDelete(id: Long){
        viewModelScope.launch (Dispatchers.IO){
            dao.undoDeleteById(id)
        }
    }
    fun deletePermanent(id: Long){
        viewModelScope.launch (Dispatchers.IO){
            dao.deleteById(id)
        }
    }
}