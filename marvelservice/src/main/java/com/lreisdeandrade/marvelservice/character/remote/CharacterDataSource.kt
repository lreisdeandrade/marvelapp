package com.lreisdeandrade.marvelservice.character.remote

import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single

interface CharacterDataSource {
    fun fetchCharacterList(offSet : Int): Single<CharacterResponse>
}
