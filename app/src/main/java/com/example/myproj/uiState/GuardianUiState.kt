package com.example.myproj.uiState

import com.example.myproj.model.ApiResult

sealed class GuardianUiState {
    object Loading : GuardianUiState()
    data class Success(val data: List<ApiResult>) : GuardianUiState()
    data class Error(val message: String) : GuardianUiState()
}