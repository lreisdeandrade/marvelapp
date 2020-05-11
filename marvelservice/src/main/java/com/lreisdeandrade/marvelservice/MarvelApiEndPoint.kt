package com.lreisdeandrade.marvelservice

enum class MarvelApiEndPoint(private val endPointName: String, host: String) {
    DEVELOP("Production", "https://gateway.marvel.com/v1/public/");

    val url: String = host

    override fun toString(): String {
        return endPointName
    }
}