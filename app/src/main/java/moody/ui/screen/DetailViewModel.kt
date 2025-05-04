package moody.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moody.database.HarianDao
import moody.model.Harian
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel (private val dao: HarianDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(judul: String, isi: String, mood: String) {
        val harian = Harian(
            tanggal = formatter.format(Date()),
            judul = judul,
            harian = isi,
            mood = mood
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(harian)
        }
    }

    suspend fun getHarian(id: Long): Harian? {
        return dao.getHarianById(id)
    }

    fun update(id: Long, judul: String, isi: String, mood: String){
        val harian = Harian(
            id = id,
            tanggal = formatter.format(Date()),
            judul = judul,
            harian = isi,
            mood = mood
        )
        viewModelScope.launch (Dispatchers.IO){
            dao.update(harian)
        }
    }
    fun softDelete(id: Long){
        viewModelScope.launch (Dispatchers.IO){
            dao.softDeleteById(id)
        }
    }
}