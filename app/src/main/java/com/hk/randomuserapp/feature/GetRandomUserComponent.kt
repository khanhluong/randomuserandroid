package com.hk.randomuserapp.feature

import com.hk.randomuserapp.di.scope.ActivityScope
import com.hk.randomuserapp.feature.randomuser.view.RandomUserListFragment
import com.hk.randomuserapp.feature.randomuserdetail.view.RandomUserDetailFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface GetRandomUserComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): GetRandomUserComponent
    }

    //inject class to component
    fun inject(fragment: RandomUserListFragment)
    fun inject(fragment: RandomUserDetailFragment)
}