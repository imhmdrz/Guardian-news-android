package com.example.myproj.uiHolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproj.repository.GuardianRepository

class GuardianViewModelFactory(private val repo: GuardianRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GuardianViewModel::class.java)){
            return GuardianViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}