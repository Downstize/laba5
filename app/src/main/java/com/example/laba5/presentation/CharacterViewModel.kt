package com.example.laba5.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.laba5.data.RetrofitInstance
import com.example.laba5.models.Character

class CharacterViewModel : ViewModel() {
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    fun fetchCharacters(page: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(page)
                _characters.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}