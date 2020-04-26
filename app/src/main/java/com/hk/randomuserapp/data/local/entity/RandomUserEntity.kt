package com.hk.randomuserapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hk.randomuserapp.utils.Constants

@Entity(tableName = Constants.TABLE_USER_NAME)
data class RandomUserEntity (
    @PrimaryKey val uid: String,
    val firstName: String,
    val lastName: String,
    val thumbnailUrl: String,
    val picUrl: String,
    val gender: String,
    val dob:String
)