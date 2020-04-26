package com.hk.randomuserapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hk.randomuserapp.data.local.entity.RandomUserEntity

@Dao
interface RandomUserDao {
    @Query("SELECT * FROM random_user")
    fun loadRandomUserList(): LiveData<List<RandomUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRandomUser(randomUserEntities: List<RandomUserEntity?>?)
}