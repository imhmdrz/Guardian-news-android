package com.example.myproj.repository


import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.model.ApiResult
import com.example.myproj.paggingSource.PagingMediator
import com.example.myproj.roomDataBase.NewsDataBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import java.io.IOException

class GuardianRepository(
    private val apiService: GuardianApiService,
    private val db: NewsDataBase,
    context: Context
) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "settings"
    )

    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianData(pageSize: Int , orderBy :String, fromDate : String): Flow<PagingData<ApiResult>> {
        Log.d("GuardianRepository", "getGuardianData: $pageSize $orderBy $fromDate ")
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PagingMediator(
                null,
                apiService,
                db,
                orderBy,
                fromDate
            ),
            pagingSourceFactory = {
                db.newsDao().getAllData()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianDataBySection(section: String, pageSize: Int, orderBy :String, fromDate : String): Flow<PagingData<ApiResult>> {
        Log.d("GuardianRepository", "getGuardianData: $pageSize $orderBy $fromDate ")
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PagingMediator(
                section,
                apiService,
                db,
                orderBy,
                fromDate
            ),
            pagingSourceFactory = {
                db.newsDao().getData(section)
            }
        ).flow
    }


    suspend fun saveToDataStore(
        numberOfItem: String? = null,
        orderBy: String?,
        fromDate: String?,
        colorTheme: String?,
        textSize: String?
    ) {
        numberOfItem?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.numberOfItem] = numberOfItem
            }
        }
        orderBy?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.orderBy] = orderBy
            }
        }
        fromDate?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.fromDate] = fromDate
            }
        }
        colorTheme?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.colorTHeme] = colorTheme
            }
        }
        textSize?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.textSize] = textSize
            }
        }
    }

    val numberOI: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val numberOfItem = preferences[PreferencesKeys.numberOfItem] ?: "10"
        numberOfItem
    }
    val orderB: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val orderBy = preferences[PreferencesKeys.orderBy] ?: "newest"
        orderBy
    }
    val fromD: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val fromDate = preferences[PreferencesKeys.fromDate] ?: "2021-01-01"
        fromDate
    }
    val colorT: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val colorTheme = preferences[PreferencesKeys.colorTHeme] ?: "white"
        colorTheme
    }
    val textS: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val textSize = preferences[PreferencesKeys.textSize] ?: "medium"
        textSize
    }

    private object PreferencesKeys {
        val numberOfItem = preferencesKey<String>("number_of_item")
        val orderBy = preferencesKey<String>("order_by")
        val fromDate = preferencesKey<String>("from_date")
        val colorTHeme = preferencesKey<String>("color_theme")
        val textSize = preferencesKey<String>("text_size")
    }
}