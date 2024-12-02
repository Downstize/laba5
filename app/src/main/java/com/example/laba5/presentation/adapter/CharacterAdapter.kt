package com.example.laba5.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.R
import com.example.laba5.models.Character

class CharacterAdapter(private var characters: List<Character> = listOf()) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.character_name)
        private val culture: TextView = itemView.findViewById(R.id.character_culture)
        private val born: TextView = itemView.findViewById(R.id.character_born)
        private val titles: TextView = itemView.findViewById(R.id.character_titles)
        private val aliases: TextView = itemView.findViewById(R.id.character_aliases)
        private val playedBy: TextView = itemView.findViewById(R.id.character_played_by)

        fun bind(character: Character) {
            name.text = character.name ?: "Unknown"
            culture.text = character.culture ?: "Unknown"
            born.text = if (!character.born.isNullOrEmpty()) "Born: ${character.born}" else "Born: Unknown"
            titles.text = if (!character.titles.isNullOrEmpty()) "Titles: ${character.titles.joinToString(", ")}" else "Titles: None"
            aliases.text = if (!character.aliases.isNullOrEmpty()) "Aliases: ${character.aliases.joinToString(", ")}" else "Aliases: None"
            playedBy.text = if (!character.playedBy.isNullOrEmpty()) "Played By: ${character.playedBy.joinToString(", ")}" else "Played By: None"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    fun updateData(newCharacters: List<Character>) {
        characters = newCharacters
        notifyDataSetChanged()
    }
}