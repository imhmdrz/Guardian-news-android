package com.example.myproj.uiState

import androidx.paging.PagingData
import com.example.myproj.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class GuardianUiState {

    object Loading : GuardianUiState()
    data class Success(var data: ApiResult) : GuardianUiState()
    data class Error(val message: String) : GuardianUiState()
}