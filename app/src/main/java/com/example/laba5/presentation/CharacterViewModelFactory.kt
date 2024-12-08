//package com.example.laba5.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.laba5.data.repository.CharacterRepository
//
//class CharacterViewModelFactory(
//    private val repository: CharacterRepository
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
//            return CharacterViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}