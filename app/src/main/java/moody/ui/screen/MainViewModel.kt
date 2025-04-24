package screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import moody.database.HarianDao
import moody.model.Harian

class MainViewModel(dao: HarianDao) : ViewModel() {
    val data: StateFlow<List<Harian>> = dao.getHarian().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    fun getHarian(id: Long): Harian? {
        return null
    }
}