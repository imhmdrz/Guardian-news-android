package com.example.myproj.uiHolder.setting

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiHolder.GuardianViewModel

class SettingViewModelFactory (
    owner: SavedStateRegistryOwner,
    private val repo: GuardianRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}