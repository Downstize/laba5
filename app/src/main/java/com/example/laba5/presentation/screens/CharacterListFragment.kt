package com.example.laba5.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.R
import com.example.laba5.presentation.CharacterViewModel
import com.example.laba5.presentation.adapter.CharacterAdapter

class CharacterListFragment : Fragment() {

    private val viewModel: CharacterViewModel by viewModels()
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Инициализация адаптера и установка его для RecyclerView
        characterAdapter = CharacterAdapter()
        recyclerView.adapter = characterAdapter

        // Подписываемся на изменения данных в ViewModel
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            // Обновляем данные адаптера
            characterAdapter.updateData(characters)
        }

        // Запрашиваем данные
        viewModel.fetchCharacters(page = 24)
    }
}