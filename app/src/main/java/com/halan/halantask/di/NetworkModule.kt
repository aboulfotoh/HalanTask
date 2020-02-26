package com.halan.halantask.di

import com.halan.halantask.network.ApiEndpointInterface
import com.halan.halantask.network.ApiUrls
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule  = module{
    single { getRetrofitClient() }
    single { getApi(get()) }
}

private fun getRetrofitClient() =
    Retrofit.Builder()
        .baseUrl(ApiUrls.BASE_URL)
        .client(OkHttpClient.Builder()/*.addInterceptor{
                var request = it.request()
                request = request.newBuilder()
                    //.addHeader("Content-Type","application/json")
                    .removeHeader("Authorization")//header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTQxNTAxNDcyNDhkODAwMTIzYWQ2OTIiLCJ1c2VyUmVxdWVzdENvbnRyb2xsIjpudWxsLCJ0eXBlIjoiVXNlciIsImlzSDM2MCI6ZmFsc2UsImlhdCI6MTU4MjE5NzgyNSwiZXhwIjoxNjEzNzU1NDI1fQ.tjG_zLonYtcKAkZdLR1jU5EkowddfB8wuIOWOiPT0yo")
                    .build()
                it.proceed(request)
        }*/.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()



private fun getApi(retrofit: Retrofit) = retrofit.create(ApiEndpointInterface::class.java)