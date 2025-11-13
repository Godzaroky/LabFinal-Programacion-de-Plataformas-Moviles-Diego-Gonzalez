package com.example.uvg.gonzalez.diego.data.network.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_DATASTORE_NAME = "settings_datastore"

val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATASTORE_NAME
)

object SettingsKeys {
    val LAST_SYNC_DATETIME = stringPreferencesKey("last_sync_datetime")
}

class SettingsRepository(private val context: Context) {

    val lastSyncDateTime: Flow<String?> =
        context.settingsDataStore.data.map { prefs ->
            prefs[SettingsKeys.LAST_SYNC_DATETIME]
        }

    suspend fun saveLastSyncDateTime(value: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.LAST_SYNC_DATETIME] = value
        }
    }
}
