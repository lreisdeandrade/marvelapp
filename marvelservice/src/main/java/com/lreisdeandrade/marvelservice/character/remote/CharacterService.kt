package com.lreisdeandrade.marvelservice.character.remote

import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single
import retrofit2.http.GET

internal interface CharacterService {

    @GET("characters")
    fun fetchCharacterList(): Single<CharacterResponse>
}