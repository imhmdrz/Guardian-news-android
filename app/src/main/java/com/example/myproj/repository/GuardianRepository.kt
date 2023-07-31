package com.example.myproj.repository


import com.example.myproj.loadDataFromInternet.GuardianApiResponse
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object  GuardianRepository {
    private val retrofitService : GuardianApiService = RetrofitIns
        .getRetrofitInstance().create(GuardianApiService::class.java)
    private var resHOME : Flow<GuardianApiResponse>? = null
    private var resWORLD : Flow<GuardianApiResponse>? = null
    private var resSCIENCE : Flow<GuardianApiResponse>? = null
    private var resSPORT : Flow<GuardianApiResponse>? = null
    private var resENVIRONMENT : Flow<GuardianApiResponse>? = null

    suspend fun getGuardianData() : Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resHOME  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }.catch { e ->
                throw Exception(e) }
        }
        return resHOME
    }
    suspend fun getGuardianDataWorld(): Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resWORLD  = flow{
                emit(retrofitService.getGuardianData("world").body()!!)
            }.catch { e ->
                throw Exception(e) }
        }
        return resWORLD
    }
    suspend fun getGuardianDataScience(): Flow<GuardianApiResponse>?{
        withContext(Dispatchers.IO){
            resSCIENCE  = flow{
                emit(retrofitService.getGuardianData("science").body()!!)
            }.catch { e ->
                throw Exception(e) }
        }
        return resSCIENCE
    }
    suspend fun getGuardianDataSport(): Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resSPORT  = flow{
                emit(retrofitService.getGuardianData("sport").body()!!)
            }.catch { e ->
                throw Exception(e) }
        }
        return resSPORT
    }
    suspend fun getGuardianDataEnvironment(): Flow<GuardianApiResponse>?{
        withContext(Dispatchers.IO){
            resENVIRONMENT  = flow{
                emit(retrofitService.getGuardianData("environment").body()!!)
            }.catch { e ->
                throw Exception(e) }
        }
        return resENVIRONMENT
    }
}