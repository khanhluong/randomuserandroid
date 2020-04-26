package com.hk.randomuserapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adding common parameter to the request
 */
class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", ApiConstants.API_KEY)
            .build()

        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}