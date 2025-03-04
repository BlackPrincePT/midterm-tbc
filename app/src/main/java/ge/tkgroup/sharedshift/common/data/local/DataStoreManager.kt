package ge.tkgroup.sharedshift.common.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.tkgroup.sharedshift.common.data.local.DataStoreConstants.DATA_STORE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun <T> observeData(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data
            .map {
                it[key]
            }
    }

    suspend fun <T> readData(key: Preferences.Key<T>): T? {
        return context.dataStore.data
            .map {
                it[key]
            }
            .first()
    }

    suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        context.dataStore
            .edit {
                it[key] = value
            }
    }

    suspend fun <T> deleteData(key: Preferences.Key<T>) {
        context.dataStore
            .edit {
                it.remove(key)
            }
    }
}