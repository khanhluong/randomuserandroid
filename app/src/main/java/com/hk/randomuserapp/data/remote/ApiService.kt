package com.hk.randomuserapp.data.remote

import androidx.lifecycle.LiveData
import com.hk.randomuserapp.data.remote.model.RandomUserResponse
import com.hk.randomuserapp.data.repository.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The ApiService contain the REST api call
 */
interface ApiService {
    @GET("/api")
    fun getRandomUser(@Query("page") page: Int,
                      @Query("results") results: Int) : LiveData<ApiResponse<RandomUserResponse>>
}