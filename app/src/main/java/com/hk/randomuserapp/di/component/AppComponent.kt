package com.hk.randomuserapp.di.component

import android.content.Context
import com.hk.randomuserapp.di.module.AppModule
import com.hk.randomuserapp.di.module.RoomModule
import com.hk.randomuserapp.feature.GetRandomUserComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * initializes all the dependent modules
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        RoomModule::class,
        AppSubcomponents::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun getRandomUserComponent(): GetRandomUserComponent.Factory

}