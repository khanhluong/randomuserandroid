package com.hk.randomuserapp.data.repository

import androidx.room.Ignore

data class RandomUserRequest(
    var page: Int,
    var gender: String
) {
    @Ignore
    constructor() : this(1, "")
}