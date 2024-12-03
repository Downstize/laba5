package com.example.laba5.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.R
import com.example.laba5.presentation.CharacterViewModel
import com.example.laba5.presentation.adapter.CharacterAdapter
import com.google.gson.Gson

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

        characterAdapter = CharacterAdapter()
        recyclerView.adapter = characterAdapter

        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            characterAdapter.updateData(characters)

            val gson = Gson()
            val charactersJson = gson.toJson(characters)

            val settingsButton = view.findViewById<View>(R.id.settings_button)
            settingsButton.setOnClickListener {
                val action = CharacterListFragmentDirections
                    .actionCharacterListFragmentToSettingsFragment(charactersJson)
                findNavController().navigate(action)
            }
        }

        viewModel.fetchCharacters(page = 24)
    }
}