package com.halan.halantask.data.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Service(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("pic")
    var pic: String = "",
    @SerializedName("prefix")
    var prefix: String = "",
    @SerializedName("terms")
    var terms: String = "",
    @SerializedName("termsKey")
    var termsKey: String = "",
    @SerializedName("vehicleType")
    var vehicleType: String = ""
) : Parcelable