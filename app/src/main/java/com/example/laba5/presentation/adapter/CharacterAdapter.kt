package com.example.laba5.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.R
import com.example.laba5.models.Character

class CharacterAdapter :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.character_name)
        private val cultureTextView: TextView = itemView.findViewById(R.id.character_culture)
        private val bornTextView: TextView = itemView.findViewById(R.id.character_born)
        private val titlesTextView: TextView = itemView.findViewById(R.id.character_titles)
        private val aliasesTextView: TextView = itemView.findViewById(R.id.character_aliases)
        private val playedByTextView: TextView = itemView.findViewById(R.id.character_played_by)

        fun bind(character: Character) {
            nameTextView.text = character.name ?: "Unknown"
            cultureTextView.text = character.culture ?: "Unknown"
            bornTextView.text = character.born ?: "Date of birth unknown"
            titlesTextView.text = character.titles?.joinToString(", ") ?: "No Titles"
            aliasesTextView.text = character.aliases?.joinToString(", ") ?: "No Aliases"
            playedByTextView.text = character.playedBy?.joinToString(", ") ?: "Unknown Actor"
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}