package com.example.myproj.utils

import com.example.myproj.model.ApiResult
import com.example.myproj.model.ThumbnailFields

fun apiResultsERROR() = List(1) {
    ApiResult("NO INTERNET CONNECTION", "", "", "", "", "", "", "", ThumbnailFields("",""), false, "", "")
}