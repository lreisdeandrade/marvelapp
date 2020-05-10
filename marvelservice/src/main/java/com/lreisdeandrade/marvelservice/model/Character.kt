package com.lreisdeandrade.marvelservice.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
//@Entity
data class Character(
//    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("thumbnail")
//    @Embedded(prefix = "character_thumbnail_")
    val thumbnail: Thumbnail,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = ""
) : Parcelable