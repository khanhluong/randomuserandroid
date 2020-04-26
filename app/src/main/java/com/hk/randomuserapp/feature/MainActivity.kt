package com.hk.randomuserapp.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hk.randomuserapp.R
import com.hk.randomuserapp.RandomUserApplication
import com.hk.randomuserapp.feature.randomuser.view.RandomUserListFragment

class MainActivity : AppCompatActivity() {

    lateinit var getRandomUserComponent: GetRandomUserComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getRandomUserComponent = (application as RandomUserApplication).appComponent.getRandomUserComponent().create()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_root, RandomUserListFragment.newInstance())
            .commit()
    }

}
