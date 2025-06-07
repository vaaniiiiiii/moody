package moody.ui.screen

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

class MainViewModel(private val dao: HarianDao) : ViewModel() {

    var dataDaily = mutableStateOf(emptyList<Gambar>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
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