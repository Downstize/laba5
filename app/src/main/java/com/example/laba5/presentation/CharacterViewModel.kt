package com.example.laba5.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.laba5.data.repository.CharacterRepository



class CharacterViewModel(context: Context) : ViewModel() {
    private val repository = CharacterRepository(context)

    val characters = liveData {
        val data = repository.getCharactersFromDb()
        emit(data)
    }

    suspend fun fetchAndSaveCharacters() {
        val apiCharacters = repository.getCharacters()
        apiCharacters?.let { repository.saveCharactersToDb(it) }
    }
}