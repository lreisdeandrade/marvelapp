package com.lreisdeandrade.marvelservice.dao

import androidx.room.*
import com.lreisdeandrade.marvelservice.model.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: Character): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(characters: List<Character>): List<Long>

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(character: Character): Int

    @Delete
    fun delete(character: Character): Int

    @Query("SELECT * FROM character")
    fun getAll(): List<Character>

    @Query("SELECT * FROM character WHERE id = :id")
    fun findById(id: Int): Character

    @Query("DELETE FROM character WHERE id = :id")
    fun deleteById(id: Int)
}