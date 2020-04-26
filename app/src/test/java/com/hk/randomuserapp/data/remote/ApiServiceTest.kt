package com.hk.randomuserapp.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hk.randomuserapp.LiveDataTestUtil.getOrAwaitValue
import com.hk.randomuserapp.data.remote.model.RandomUserResponse
import com.hk.randomuserapp.data.repository.ApiSuccessResponse
import com.hk.randomuserapp.data.repository.LiveDataCallAdapterFactory
import com.hk.randomuserapp.utils.Constants
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

@RunWith(JUnit4::class)
class ApiServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ApiService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRandomUser() {
        enqueueResponse("random_user.json")
        val response = (service.getRandomUser(1, Constants.NUMBER_USER_EACH_PAGE).getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, CoreMatchers.`is`("/api?page=1&results=10"))

        assertThat<RandomUserResponse>(response, IsNull.notNullValue())
        assertThat(response.info, IsNull.notNullValue())
        assertEquals(response.info.page, 1)
        assertEquals(response.info.results, Constants.NUMBER_USER_EACH_PAGE)

        assertThat(response.results, IsNull.notNullValue())
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream("api-response/$fileName"))
        val source = reader.readText()
        reader.close()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source)
        )
    }
}