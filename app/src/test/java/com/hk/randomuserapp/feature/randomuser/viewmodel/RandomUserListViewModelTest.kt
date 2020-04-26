package com.hk.randomuserapp.feature.randomuser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hk.randomuserapp.data.repository.RandomUserRepository
import com.hk.randomuserapp.utils.mock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class RandomUserListViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private var repository = mock(RandomUserRepository::class.java)
    private var viewModel = RandomUserListViewModel(repository)

    @Test
    fun repository_SHOULD_call_loadRandomUsers_WHEN_getListRandomUserLiveData() {
        val page = 1

        val request = viewModel.randomUserRequest(page)
//        doReturn(request).`when`(viewModel).randomUserRequest(page)
        doNothing().`when`(repository.loadRandomUsers(viewModel.randomUserRequest(page)))

        viewModel.getListRandomUserLiveData(page).observeForever(mock())
        viewModel.getListRandomUserLiveData(page)
        verify(repository).loadRandomUsers(viewModel.randomUserRequest(page))
    }
}