package com.halan.halantask.data.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Category(
    @SerializedName("naming")
    var naming: String = "",
    @SerializedName("pic")
    var pic: String = "",
    @SerializedName("prefix")
    var prefix: String = "",
    @SerializedName("services")
    var services: ArrayList<Service> = arrayListOf()
) : Parcelable