package com.hk.randomuserapp.di.component

import com.hk.randomuserapp.feature.GetRandomUserComponent
import dagger.Module

@Module(subcomponents = [GetRandomUserComponent::class])
class AppSubcomponents