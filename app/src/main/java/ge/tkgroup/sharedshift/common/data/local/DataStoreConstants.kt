package ge.tkgroup.sharedshift.common.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstants {
    const val DATA_STORE_NAME = "preferences"

    val AUTH_STATE_KEY = booleanPreferencesKey(name = "auth_state")
    val SHARED_SHIFT_KEY = stringPreferencesKey(name = "shared_shift")
}