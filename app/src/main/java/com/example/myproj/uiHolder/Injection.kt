/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myproj.uiHolder

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.roomDataBase.NewsDataBase
object Injection {

    private fun provideGithubRepository(context: Context): GuardianRepository {
        return GuardianRepository(
            apiService = RetrofitIns.getRetrofitInstance().create(GuardianApiService::class.java),
            db = NewsDataBase.getInstance(context),
            context = context
        )
    }
    fun provideViewModelFactory(context: Context, owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return GuardianViewModelFactory(owner, provideGithubRepository(context))
    }
}
