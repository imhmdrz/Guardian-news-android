package com.example.myproj.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val LAYOUT_PREFERENCES_NAME = "setting"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)
object PreferencesKeys {
    val numberOfItem = stringPreferencesKey("number_of_item")
    val orderBy = stringPreferencesKey("order_by")
    val fromDate = stringPreferencesKey("from_date")
    val colorTHeme = stringPreferencesKey("color_theme")
    val textSize = stringPreferencesKey("text_size")
}
