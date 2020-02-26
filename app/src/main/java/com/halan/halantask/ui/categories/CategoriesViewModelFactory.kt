package com.halan.halantask.ui.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.halan.halantask.network.ApiEndpointInterface

class CategoriesViewModelFactory(private val apiEndpointInterface: ApiEndpointInterface,private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(
                apiEndpointInterface,context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}