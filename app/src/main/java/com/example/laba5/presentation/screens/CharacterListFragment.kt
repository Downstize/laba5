package com.example.laba5.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.R
import com.example.laba5.data.ApiService
import com.example.laba5.data.local.database.AppDatabase
import com.example.laba5.data.repository.CharacterRepository
import com.example.laba5.models.Character
import com.example.laba5.presentation.adapter.CharacterAdapter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CharacterListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var characterRepository: CharacterRepository

    private lateinit var pageNumberText: TextView
    private lateinit var previousPageButton: Button
    private lateinit var updateButton: Button
    private lateinit var nextPageButton: Button

    private var currentPage: Int = 1
    private var allCharacters: List<Character> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character_list, container, false)

        characterRepository = CharacterRepository(requireContext())

        recyclerView = view.findViewById(R.id.recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        pageNumberText = view.findViewById(R.id.page_number_text)
        previousPageButton = view.findViewById(R.id.previous_page_button)
        updateButton = view.findViewById(R.id.update_button)
        nextPageButton = view.findViewById(R.id.next_page_button)

        characterAdapter = CharacterAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = characterAdapter

        // Кнопка обновления
        updateButton.setOnClickListener {
            fetchCharactersFromApi()
        }

        // Кнопка "Предыдущая страница"
        previousPageButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateDisplayedCharacters()
            }
        }

        // Кнопка "Следующая страница"
        nextPageButton.setOnClickListener {
            if (currentPage * 50 < allCharacters.size) {
                currentPage++
                updateDisplayedCharacters()
            }
        }

        observeCharacters()

        return view
    }

    private fun observeCharacters() {
        lifecycleScope.launchWhenStarted {
            characterRepository.observeCharacters().collect { characterEntities ->
                allCharacters = characterEntities.map {
                    Character(
                        it.name,
                        it.culture,
                        it.born,
                        it.titles,
                        it.aliases,
                        it.playedBy
                    )
                }
                updateDisplayedCharacters()
            }
        }
    }

    private fun fetchCharactersFromApi() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val characters = characterRepository.getCharacters()
            if (!characters.isNullOrEmpty()) {
                characterRepository.saveCharactersToDb(characters)
            }
            progressBar.visibility = View.GONE
        }
    }

    private fun updateDisplayedCharacters() {
        val fromIndex = (currentPage - 1) * 50
        val toIndex = (fromIndex + 50).coerceAtMost(allCharacters.size)

        val subList = if (fromIndex < allCharacters.size) {
            allCharacters.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }

        characterAdapter.submitList(subList)
        updatePageNumber(currentPage)

        updateButtonState()
    }

    private fun updatePageNumber(page: Int) {
        pageNumberText.text = "Страница: $page"
    }

    private fun updateButtonState() {
        previousPageButton.isEnabled = currentPage > 1
        nextPageButton.isEnabled = currentPage * 50 < allCharacters.size
    }
}