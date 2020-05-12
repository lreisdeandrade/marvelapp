package com.lreisdeandrade.marvelservice.character.remote

import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CharacterService {

    @GET("characters")
    fun fetchCharacterList(@Query("offset") offSet: Int): Single<CharacterResponse>
}