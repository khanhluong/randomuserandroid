package com.hk.randomuserapp.feature.randomuserdetail.viewmodel

import androidx.lifecycle.ViewModel
import com.hk.randomuserapp.data.repository.RandomUserRepository
import com.hk.randomuserapp.di.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class RandomUserDetailViewModel @Inject constructor(private val repository: RandomUserRepository) :
    ViewModel() {
}