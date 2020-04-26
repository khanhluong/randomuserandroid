package com.hk.randomuserapp.data.repository

import androidx.lifecycle.LiveData
import com.android.example.github.util.RateLimiter
import com.hk.randomuserapp.AppExecutors
import com.hk.randomuserapp.data.local.dao.RandomUserDao
import com.hk.randomuserapp.data.local.entity.RandomUserEntity
import com.hk.randomuserapp.data.remote.ApiService
import com.hk.randomuserapp.data.remote.model.RandomUserResponse
import com.hk.randomuserapp.data.remote.model.Result
import com.hk.randomuserapp.utils.Constants
import java.util.concurrent.TimeUnit

open class RandomUserRepository constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val randomUserDao: RandomUserDao
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)
    fun loadRandomUsers(request: RandomUserRequest): LiveData<Resource<List<RandomUserEntity>>> {
        return object :
            NetworkBoundResource<List<RandomUserEntity>, RandomUserResponse>(appExecutors) {
            override fun saveCallResult(randomUserResponse: RandomUserResponse) {
                val users: MutableList<RandomUserEntity> = ArrayList()
                val results = randomUserResponse.results
                results.forEach {
                    val user = it.toRandomUser()
                    users.add(user)
                }
                randomUserDao.saveRandomUser(users)
            }

            override fun shouldFetch(data: List<RandomUserEntity>?): Boolean {
                return true
            }

            override fun loadFromDb() = randomUserDao.loadRandomUserList()

            override fun onFetchFailed() {
                repoListRateLimit.reset(request.page.toString())
            }

            override fun createCall() =
                apiService.getRandomUser(request.page, Constants.NUMBER_USER_EACH_PAGE)
        }.asLiveData()

    }

    fun Result.toRandomUser() = RandomUserEntity(
        uid = id.name,
        dob = dob.date,
        firstName = name.first,
        lastName = name.last,
        gender = gender,
        thumbnailUrl = picture.thumbnail,
        picUrl = picture.large
    )
}