package com.example.laba5.data.local.dao

import androidx.room.*
import com.example.laba5.data.local.database.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>): List<Long>

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

}