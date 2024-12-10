package com.example.laba5.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laba5.R
import com.example.laba5.data.repository.CharacterRepository
import com.example.laba5.databinding.FragmentCharacterListBinding
import com.example.laba5.models.Character
import com.example.laba5.presentation.adapter.CharacterAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var characterRepository: CharacterRepository

    private var currentPage: Int = 1
    private var allCharacters: List<Character> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        characterRepository = CharacterRepository(requireContext())

        characterAdapter = CharacterAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = characterAdapter

        binding.updateButton.setOnClickListener {
            fetchCharactersFromApi()
        }

        binding.previousPageButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateDisplayedCharacters()
            }
        }


        binding.nextPageButton.setOnClickListener {

                currentPage++
                updateDisplayedCharacters()

        }

        observeCharacters()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeCharacters() {
        Log.d("CharacterListFragment", "observeCharacters called")
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            // Проверяем, есть ли данные в БД
            val charactersInDb = characterRepository.observeCharacters().first()

            if (charactersInDb.isEmpty()) {
                // Если БД пуста, загружаем данные из API
                Log.d("CharacterListFragment", "No characters in DB, fetching from API")
                val apiCharacters = characterRepository.getCharacters()
                if (!apiCharacters.isNullOrEmpty()) {
                    characterRepository.saveCharactersToDb(apiCharacters)
                    allCharacters = apiCharacters
                    currentPage = 1 // Сбрасываем страницу после загрузки данных
                    updateDisplayedCharacters()
                } else {
                    Log.d("CharacterListFragment", "No characters fetched from API")
                }
            } else {
                // Если данные есть в БД, отображаем их
                Log.d("CharacterListFragment", "Loaded ${charactersInDb.size} characters from DB")
                allCharacters = charactersInDb.map { entity ->
                    Character(
                        name = entity.name,
                        culture = entity.culture,
                        born = entity.born,
                        titles = entity.titles,
                        aliases = entity.aliases,
                        playedBy = entity.playedBy
                    )
                }
                currentPage = 1 // Сбрасываем страницу при загрузке из базы
                updateDisplayedCharacters()
            }

            binding.progressBar.visibility = View.GONE
        }
    }





    private fun fetchCharactersFromApi() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val characters = characterRepository.getCharacters()
            if (!characters.isNullOrEmpty()) {
                characterRepository.saveCharactersToDb(characters)
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun fetchCharactersForPage(page: Int) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val newCharacters = characterRepository.getCharactersByPage(page) // API поддерживает пагинацию
            if (!newCharacters.isNullOrEmpty()) {
                Log.d("CharacterListFragment", "Fetched ${newCharacters.size} characters for page $page")

                // Добавляем данные в общий список
                allCharacters = allCharacters + newCharacters
                characterRepository.saveCharactersToDb(newCharacters) // Сохраняем в базу
                updateDisplayedCharacters()
            } else {
                Log.d("CharacterListFragment", "No characters fetched for page $page")
            }
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun updateDisplayedCharacters() {
        val fromIndex = (currentPage - 1) * 50
        val toIndex = (fromIndex + 50).coerceAtMost(allCharacters.size)

        // Проверяем, есть ли данные для текущей страницы
        val subList = if (fromIndex < allCharacters.size) {
            allCharacters.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }

        if (subList.isEmpty()) {
            Log.d("CharacterListFragment", "No characters for page $currentPage, fetching from API")
            // Если данных нет, загружаем их из API
            fetchCharactersForPage(currentPage)
        } else {
            Log.d("CharacterListFragment", "Displaying characters from $fromIndex to $toIndex")
            characterAdapter.submitList(subList)
        }

        // Обновляем текст текущей страницы
        binding.pageNumberText.text = "Страница: $currentPage"

        // Управляем состоянием кнопок
        binding.previousPageButton.isEnabled = currentPage > 1
        binding.nextPageButton.isEnabled = allCharacters.size >= toIndex
    }



}
