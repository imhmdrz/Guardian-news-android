package com.example.myproj.loadDataFromInternet

import com.example.myproj.model.GuardianApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardianApiService {
    @GET("search?show-fields=thumbnail%2CtrailText&api-key=74d64ca2-bd49-4c1f-99c5-778c6af08086")
    //f0bbbffe-ebbf-499a-a2e9-ea0fa9d3d48a
    //74d64ca2-bd49-4c1f-99c5-778c6af08086
    suspend fun getGuardianData(
        @Query("section") section: String?,
        @Query("page") page: Int?,
        @Query("page-size") pageSize: Int = 10,
        @Query("order-by") orderBy: String = "newest",
        @Query("from-date") fromDate: String = "2021-01-01"
    ): Response<GuardianApiResponse>
}
