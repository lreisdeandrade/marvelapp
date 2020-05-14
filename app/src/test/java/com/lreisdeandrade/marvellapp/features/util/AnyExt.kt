package com.lreisdeandrade.marvellapp.features.util

import com.lreisdeandrade.marvelservice.MarvellModule
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

fun <T> Any.parseObject(jsonFile: String, objectType: Type): T {
    val characterRaw = javaClass.classLoader?.getResourceAsStream(jsonFile)
    val characterResponseJson = BufferedReader(InputStreamReader(characterRaw))

    return MarvellModule.gsonBuilder().fromJson(characterResponseJson, objectType as Class<T>)
}
