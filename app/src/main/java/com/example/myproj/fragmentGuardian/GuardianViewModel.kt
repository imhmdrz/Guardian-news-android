package com.example.myproj.fragmentGuardian

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.myproj.repository.GuardianRepository


class GuardianViewModel@JvmOverloads constructor(
    application: Application,
    private val repository: GuardianRepository = GuardianRepository.getInstance(application)
) : AndroidViewModel(application){

}