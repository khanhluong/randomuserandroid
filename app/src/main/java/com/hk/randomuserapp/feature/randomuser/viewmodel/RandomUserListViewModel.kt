package com.hk.randomuserapp.feature.randomuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hk.randomuserapp.data.local.entity.RandomUserEntity
import com.hk.randomuserapp.data.repository.RandomUserRepository
import com.hk.randomuserapp.data.repository.RandomUserRequest
import com.hk.randomuserapp.data.repository.Resource
import com.hk.randomuserapp.feature.randomuser.adapter.RandomUserRecycleViewAdapter
import javax.inject.Inject

class RandomUserListViewModel @Inject constructor(private val repository: RandomUserRepository) :
    ViewModel() {

//    private val users: MutableLiveData<Resource<List<RandomUserEntity>>> by lazy {
//        MutableLiveData().also {
//            loadUsers()
//        }
//    }

    fun getListRandomUserLiveData(page: Int): LiveData<Resource<List<RandomUserEntity>>>{
        val request = randomUserRequest(page)
        return repository.loadRandomUsers(request)
    }

    fun randomUserRequest(page: Int): RandomUserRequest {
        val request = RandomUserRequest()
        request.page = page
        return request
    }

    fun filterRandomUserData(adapter: RandomUserRecycleViewAdapter, query: String){
        adapter.filter.filter(query)
    }

    fun resetDataFilter(adapter: RandomUserRecycleViewAdapter){
        adapter.resetFilter()
    }
}
