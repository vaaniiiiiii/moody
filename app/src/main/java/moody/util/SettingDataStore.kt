package moody.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = "setting_preference"
)
class SettingDataStore(private val context: Context) {
    companion object{
        private val IS_LIST = booleanPreferencesKey("is_list")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIST] ?: true
    }
}