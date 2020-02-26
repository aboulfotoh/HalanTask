package com.halan.halantask.ui.categories

import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halan.halantask.data.model.Categories
import com.halan.halantask.network.ApiEndpointInterface
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class CategoriesViewModel(
    private val apiEndpointInterface: ApiEndpointInterface,
    context: Context
) : ViewModel() {

    private val _categories = MutableLiveData<Categories>()
    val categories: LiveData<Categories> = _categories

    init {
        if (Hawk.contains("Categories")){
            loadCategories()
        } else
            _categories.value = Categories()
    }

    fun getCategories(location: Location) {
        val auth =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZTQxNTAxNDcyNDhkODAwMTIzYWQ2OTIiLCJ1c2VyUmVxdWVzdENvbnRyb2xsIjpudWxsLCJ0eXBlIjoiVXNlciIsImlzSDM2MCI6ZmFsc2UsImlhdCI6MTU4MjE5NzgyNSwiZXhwIjoxNjEzNzU1NDI1fQ.tjG_zLonYtcKAkZdLR1jU5EkowddfB8wuIOWOiPT0yo"
        val lang = Locale.getDefault().language
        val device = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE
        val version = VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name
        viewModelScope.launch {
            try {
                _categories.value = apiEndpointInterface.getCategories(
                    auth,
                    lang,
                    location.longitude.toString(),
                    location.latitude.toString(),
                    device,
                    version
                ).await()
            } catch (t: Throwable) {
                Log.e("Error", t.message)
            }
        }
    }

    fun loadCategories() {
        _categories.value = Hawk.get("Categories")
    }
}