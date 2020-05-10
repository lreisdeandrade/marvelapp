package com.lreisdeandrade.marvelservice.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//@Entity
@Parcelize
data class Thumbnail(
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("extension")
    val extension: String? = null
) : Parcelable