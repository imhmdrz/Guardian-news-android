package com.example.myproj.loadDataFromInternet

data class GuardianApiResponse(
    val response: ApiResponseData?
)

data class ApiResponseData(
    val status: String,
    val userTier: String,
    val total: Int,
    val startIndex: Int,
    val pageSize: Int,
    val currentPage: Int,
    val pages: Int,
    val orderBy: String,
    val results: List<ApiResult>
)

data class ApiResult(
    val id: String,
    val type: String,
    val sectionId: String,
    val sectionName: String,
    val webPublicationDate: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val fields: ThumbnailFields,
    val isHosted: Boolean,
    val pillarId: String,
    val pillarName: String
)

data class ThumbnailFields(
    val thumbnail: String
)
