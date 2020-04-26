package com.hk.randomuserapp

import android.app.Application
import com.hk.randomuserapp.di.component.AppComponent
import com.hk.randomuserapp.di.component.DaggerAppComponent

open class RandomUserApplication : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}