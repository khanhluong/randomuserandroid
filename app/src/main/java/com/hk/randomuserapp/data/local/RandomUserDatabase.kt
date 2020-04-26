package com.hk.randomuserapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hk.randomuserapp.BuildConfig.VERSION_CODE
import com.hk.randomuserapp.data.local.dao.RandomUserDao
import com.hk.randomuserapp.data.local.entity.RandomUserEntity

@Database(
    entities = [RandomUserEntity::class],
    version = VERSION_CODE
)
abstract class RandomUserDatabase : RoomDatabase() {
    abstract fun randomUserDao(): RandomUserDao
}