package com.lreisdeandrade.marvelservice.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lreisdeandrade.marvelservice.model.Character

@Database(entities = arrayOf(Character::class), version = 1)
abstract class CharacterDataBase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}