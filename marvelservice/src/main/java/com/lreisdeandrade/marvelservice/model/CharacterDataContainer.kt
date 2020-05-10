package com.lreisdeandrade.marvelservice.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDataContainer(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("results")
    val results: ArrayList<Character>?
) : Parcelable