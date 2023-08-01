package com.example.myproj.loadDataFromInternet

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardianApiService {
    @GET("search?show-fields=thumbnail&api-key=f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a")
    suspend fun getGuardianData(
        @Query("section") section: String?
    ): Response<GuardianApiResponse>

}
