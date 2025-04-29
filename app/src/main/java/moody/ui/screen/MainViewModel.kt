package moody.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moody.database.HarianDao
import moody.model.Harian

class MainViewModel(private val dao: HarianDao) : ViewModel() {

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