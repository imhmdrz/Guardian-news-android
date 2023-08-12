package com.example.myproj.uiHolder

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.myproj.repository.GuardianRepository

class GuardianViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repo: GuardianRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(GuardianViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GuardianViewModel(repo, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}