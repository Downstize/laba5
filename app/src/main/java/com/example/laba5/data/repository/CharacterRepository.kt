package com.example.laba5.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Room
import com.example.laba5.data.ApiClient
import com.example.laba5.data.local.database.AppDatabase
import com.example.laba5.data.local.database.CharacterEntity
import com.example.laba5.models.Character
import kotlinx.coroutines.flow.Flow
import java.io.File

class CharacterRepository(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "character_database"
    ).build()

    private val characterDao = database.characterDao()
    private val apiService = ApiClient.apiService

    suspend fun getCharacters(): List<Character>? {
        return try {
            val response = apiService.getCharacters()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("CharacterRepository", "Error: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Exception: ${e.message}")
            emptyList()
        }
    }

    fun saveHeroesToFile(context: Context, heroes: List<String?>, fileName: String): Boolean {
        val externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!externalStorage.exists()) {
            externalStorage.mkdir()
        }
        val file = File(externalStorage, fileName)
        return try {
            file.writeText(heroes.joinToString("\n"))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun saveCharactersToDb(characters: List<Character>) {
        val entities = characters.map {
            CharacterEntity(
                name = it.name,
                culture = it.culture,
                born = it.born,
                titles = it.titles,
                aliases = it.aliases,
                playedBy = it.playedBy
            )
        }
        characterDao.insertCharacters(entities)
    }

    suspend fun getCharactersFromDb(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }

    suspend fun clearDatabase() {
        characterDao.deleteAllCharacters()
    }

    fun observeCharacters(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }

    suspend fun getCharactersByPage(page: Int): List<Character>? {
        return try {
            val response = apiService.getCharacters(page)
            if (response.isSuccessful) response.body() else emptyList()
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Exception: ${e.message}")
            emptyList()
        }
    }


}