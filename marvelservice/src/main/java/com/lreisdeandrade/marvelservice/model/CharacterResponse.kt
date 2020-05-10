package com.lreisdeandrade.marvelservice.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(@SerializedName("copyright")
                             val copyright: String = "",
                             @SerializedName("code")
                             val code: Int = 0,
                             @SerializedName("data")
                             val characterDataContainer: CharacterDataContainer,
                             @SerializedName("attributionHTML")
                             val attributionHTML: String = "",
                             @SerializedName("attributionText")
                             val attributionText: String = "",
                             @SerializedName("etag")
                             val etag: String = "",
                             @SerializedName("status")
                             val status: String = "")