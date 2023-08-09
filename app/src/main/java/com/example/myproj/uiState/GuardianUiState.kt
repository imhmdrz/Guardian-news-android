package com.example.myproj.uiState

import androidx.paging.PagingData
import com.example.myproj.model.ApiResult
import kotlinx.coroutines.flow.Flow

sealed class GuardianUiState {
    object Loading : GuardianUiState()
    data class Success(val data: Flow<PagingData<ApiResult>>) : GuardianUiState()
    data class Error(val message: String) : GuardianUiState()
}