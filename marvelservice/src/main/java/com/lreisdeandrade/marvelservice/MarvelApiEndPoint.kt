package com.lreisdeandrade.marvelservice

enum class MarvelApiEndPoint(private val endPointName: String, host: String) {
    PROD("Production", "https://gateway.marvel.com/v1/public/");
//    DEVELOP("Develop", "developUrl", "wcs/");

    val url: String = host

    override fun toString(): String {
        return endPointName
    }
}