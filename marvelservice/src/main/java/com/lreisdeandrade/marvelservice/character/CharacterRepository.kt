package com.lreisdeandrade.marvelservice.character

import com.lreisdeandrade.marvelservice.character.remote.CharacterDataSource
import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single

class CharacterRepository(private val characterDataSource: CharacterDataSource) :
    CharacterDataSource {
    override fun fetchCharacterList(): Single<CharacterResponse> {
        return characterDataSource.fetchCharacterList()
    }
}