package com.lreisdeandrade.marvelservice.character.remote

import com.lreisdeandrade.marvelservice.MarvellModule
import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single
import timber.log.Timber
object CharacterApi : CharacterDataSource {

    private val marvellService: CharacterService

    init {
        val retrofit = MarvellModule.retrofit
        marvellService = retrofit.create(
            CharacterService::class.java)
    }

    override fun fetchCharacterList(): Single<CharacterResponse> {
        return marvellService.fetchCharacterList()
            .doOnError { Timber.e(it, "fetchCharacterList: %s", it.message) }
    }
}