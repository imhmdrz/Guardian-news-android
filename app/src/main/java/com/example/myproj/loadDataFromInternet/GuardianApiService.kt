package com.example.myproj.loadDataFromInternet

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardianApiService {
    @GET("search?show-fields=thumbnail%2CtrailText&api-key=74d64ca2-bd49-4c1f-99c5-778c6af08086")
    suspend fun getGuardianData(
        @Query("section") section: String?
    ): Response<GuardianApiResponse>
}
