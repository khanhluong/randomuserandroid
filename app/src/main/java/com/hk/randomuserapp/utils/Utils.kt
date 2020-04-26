package com.hk.randomuserapp.utils

import com.hk.randomuserapp.data.local.entity.RandomUserEntity

object Utils {

    fun createResultTempObject(): RandomUserEntity {
        return RandomUserEntity("", "","","","","", "")
    }
}