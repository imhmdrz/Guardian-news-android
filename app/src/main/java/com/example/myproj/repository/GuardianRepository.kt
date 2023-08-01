package com.example.myproj.repository


import android.widget.Toast
import com.example.myproj.loadDataFromInternet.GuardianApiResponse
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object GuardianRepository {
    private val retrofitService: GuardianApiService = RetrofitIns
        .getRetrofitInstance().create(GuardianApiService::class.java)
    private var res: Flow<GuardianApiResponse>? = null
    suspend fun getGuardianData(str: String?): Flow<GuardianApiResponse>? = withContext(Dispatchers.IO) {
            res = flow {
                emit(retrofitService.getGuardianData(str).body()!!)
            }.catch {
                emit(GuardianApiResponse(null))
                Toast.makeText(null, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
            res
        }
}