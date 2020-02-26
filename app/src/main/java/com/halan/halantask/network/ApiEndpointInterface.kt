package com.halan.halantask.network

import com.halan.halantask.data.model.Categories
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiEndpointInterface {

    @Headers( "Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTQxNTAxNDcyNDhkODAwMTIzYWQ2OTIiLCJ1c2VyUmVxdWVzdENvbnRyb2xsIjpudWxsLCJ0eXBlIjoiVXNlciIsImlzSDM2MCI6ZmFsc2UsImlhdCI6MTU4MjE5NzgyNSwiZXhwIjoxNjEzNzU1NDI1fQ.tjG_zLonYtcKAkZdLR1jU5EkowddfB8wuIOWOiPT0yo")
    @GET(ApiUrls.SERVICES)
    fun getCategories(@Header("Authorization")auth:String,
                      @Header("language")lang:String,
                      @Header("long")lng:String,
                      @Header("lat")lat:String,
                      @Header("device")device:String,
                      @Header("version")version:String) : Deferred<Categories>
}