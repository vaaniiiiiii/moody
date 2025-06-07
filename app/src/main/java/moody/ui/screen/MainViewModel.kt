package moody.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moody.database.HarianDao
import moody.model.Harian
import moody.network.MoodyApi

class MainViewModel(private val dao: HarianDao) : ViewModel() {

    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = MoodyApi.service.getDaily()
                Log.d("MainViewModel", "Success: $result")
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
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