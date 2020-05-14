package com.lreisdeandrade.marvellapp.features.util

import com.lreisdeandrade.marvelservice.model.Character
import com.lreisdeandrade.marvelservice.model.Thumbnail

object DummyData {

    const val VAlUE_STRING = "123456"
    const val VAlUE_INT = 0

    fun arrayListCharacter(): ArrayList<Character> = arrayListOf(getCharacter(),
        getCharacter(), getCharacter())

    fun getCharacter(): Character = Character(
        VAlUE_INT,
        getThumbnail(),
        "s",
        VAlUE_STRING
    )

    fun getThumbnail(): Thumbnail = Thumbnail(
        VAlUE_STRING,
        VAlUE_STRING
    )
}
