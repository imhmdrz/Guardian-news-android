package com.example.myproj.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class GuardianApiResponse(
    val response: ApiResponseData
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

@Entity(tableName = "resApi")
data class ApiResult(
    @PrimaryKey @field:SerializedName("id")val id: String,
    @field:SerializedName("type") val type: String,
    @field:SerializedName("sectionId")
    val sectionId: String,
    @field:SerializedName("sectionName")
    val sectionName: String,
    @field:SerializedName("webPublicationDate")
    val webPublicationDate: String,
    @field:SerializedName("webTitle")
    val webTitle: String,
    @field:SerializedName("webUrl")
    val webUrl: String,
    @field:SerializedName("apiUrl")
    val apiUrl: String,
    @field:SerializedName("fields")
    val fields: ThumbnailFields,
    @field:SerializedName("isHosted")
    val isHosted: Boolean,
    @field:SerializedName("pillarId")
    val pillarId: String,
    @field:SerializedName("pillarName")
    val pillarName: String
)

data class ThumbnailFields(
    val thumbnail: String,
    val trailText : String
)
