package com.hk.randomuserapp.di.module

import android.content.Context
import android.content.SharedPreferences
import com.hk.randomuserapp.AppExecutors
import com.hk.randomuserapp.R
import com.hk.randomuserapp.data.local.dao.RandomUserDao
import com.hk.randomuserapp.data.remote.ApiConstants
import com.hk.randomuserapp.data.remote.ApiService
import com.hk.randomuserapp.data.repository.LiveDataCallAdapterFactory
import com.hk.randomuserapp.data.repository.RandomUserRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppModule {
    @Provides
    @Singleton
    @Named("app_url")
    fun provideAppBaseUrl(): String = ApiConstants.BASE_URL

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    @Named("app_okhttp")
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @Named("app_retrofit")
    fun provideRetrofit(@Named("app_url") baseUrl: String,
                        @Named("app_okhttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppApiServices(@Named("app_retrofit") retrofit: Retrofit): ApiService = retrofit.create(
        ApiService::class.java)

    @Provides
    @Singleton
    internal fun provideRepository(appExecutors: AppExecutors,
                                   apiService: ApiService,
                                   userDao: RandomUserDao
    ): RandomUserRepository {
        return RandomUserRepository(appExecutors, apiService, userDao)
    }

    @Provides
    @Singleton
    fun provideSharePreference(context: Context): SharedPreferences{
        return context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

}