package com.lreisdeandrade.marvelapp

//import androidx.room.RoomDatabase
import com.lreisdeandrade.marvelservice.character.CharacterRepository
import com.lreisdeandrade.marvelservice.character.remote.CharacterApi
import com.lreisdeandrade.marvelservice.character.remote.CharacterDataSource
//import com.lreisdeandrade.marvelservice.dao.CharacterDataBase

/**
 * Enables injection of mock implementations for
 * This useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
object Injection {

    fun provideCharacterRepository(): CharacterDataSource {
        return CharacterRepository(CharacterApi)
    }
}