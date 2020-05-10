package com.lreisdeandrade.marvelservice.util

import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.toMd5(): String {
    try {
        val md5Encoder = MessageDigest.getInstance("MD5")
        val digest = md5Encoder.digest(this.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    } catch (e: NoSuchAlgorithmException) {
        Timber.e("can't generate api key")
    }
    return ""
}