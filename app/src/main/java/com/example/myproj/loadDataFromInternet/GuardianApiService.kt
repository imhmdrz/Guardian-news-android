package com.example.myproj.loadDataFromInternet

import retrofit2.Response
import retrofit2.http.GET

interface GuardianApiService {
    @GET("search?show-fields=thumbnail&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianData(): Response<GuardianApiResponse>

    @GET("search?show-fields=thumbnail&section=world&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianDataWorld(): Response<GuardianApiResponse>

    @GET("search?show-fields=thumbnail&section=science&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianDataScience(): Response<GuardianApiResponse>

    @GET("search?show-fields=thumbnail&section=sport&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianDataSport(): Response<GuardianApiResponse>

    @GET("search?show-fields=thumbnail&section=environment&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianDataEnvironment(): Response<GuardianApiResponse>

}
