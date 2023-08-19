package com.example.myproj.uiHolder.setting

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiHolder.GuardianViewModel

class SettingViewModelFactory (
    private val repo: GuardianRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}