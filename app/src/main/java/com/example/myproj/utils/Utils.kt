package com.example.myproj.utils

import com.example.myproj.loadDataFromInternet.ApiResult
import com.example.myproj.loadDataFromInternet.ThumbnailFields

fun apiResultsERROR() = List(1) {
    ApiResult("NO INTERNET CONNECTION", "", "", "", "", "", "", "", ThumbnailFields("",""), false, "", "")
}