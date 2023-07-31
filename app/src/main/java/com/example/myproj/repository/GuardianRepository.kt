package com.example.myproj.repository

import android.content.Context
import com.example.myproj.loadDataFromInternet.GuardianApiResponse
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GuardianRepository (private val retrofitService : GuardianApiService = RetrofitIns
    .getRetrofitInstance().create(GuardianApiService::class.java)) {

    companion object {
        private var instance: GuardianRepository? = null

        fun getInstance(context: Context): GuardianRepository {
            return instance ?: synchronized(this) {
                instance ?: GuardianRepository().also {
                    instance = it
                }
            }
        }
    }
    private var resHOME : Flow<GuardianApiResponse>? = null
    private var resWORLD : Flow<GuardianApiResponse>? = null
    private var resSCIENCE : Flow<GuardianApiResponse>? = null
    private var resSPORT : Flow<GuardianApiResponse>? = null
    private var resENVIRONMENT : Flow<GuardianApiResponse>? = null

    suspend fun getGuardianData() : Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resHOME  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }
        }
        return resHOME
    }
    suspend fun getGuardianDataWorld(): Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resWORLD  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }
        }
        return resWORLD
    }
    suspend fun getGuardianDataScience(): Flow<GuardianApiResponse>?{
        withContext(Dispatchers.IO){
            resSCIENCE  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }
        }
        return resSCIENCE
    }
    suspend fun getGuardianDataSport(): Flow<GuardianApiResponse>? {
        withContext(Dispatchers.IO){
            resSPORT  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }
        }
        return resSPORT
    }
    suspend fun getGuardianDataEnvironment(): Flow<GuardianApiResponse>?{
        withContext(Dispatchers.IO){
            resENVIRONMENT  = flow{
                emit(retrofitService.getGuardianData().body()!!)
            }
        }
        return resENVIRONMENT
    }
}