package com.hk.randomuserapp.di.module

import android.content.Context
import androidx.room.Room
import com.hk.randomuserapp.data.local.RandomUserDatabase
import com.hk.randomuserapp.data.local.dao.RandomUserDao
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): RandomUserDatabase {
        return Room.databaseBuilder(context, RandomUserDatabase::class.java, "random_user.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: RandomUserDatabase): RandomUserDao {
        return database.randomUserDao()
    }

    @Provides
    @Singleton
    @Named("str")
    fun provideString(): String{
        return "str"
    }
}