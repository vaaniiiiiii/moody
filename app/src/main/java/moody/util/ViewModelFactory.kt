package moody.util

import android.content.Context
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import moody.database.HarianDb
import moody.ui.screen.DetailViewModel
import moody.ui.screen.MainViewModel

class ViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory{
    @Suppress("uncheked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = HarianDb.getInstance(context).dao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dao) as T
        }
        throw illegalDecoyCallException("unknown ViewModel class")
    }
}