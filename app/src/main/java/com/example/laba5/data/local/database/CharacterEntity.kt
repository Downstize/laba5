package com.example.laba5.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    val name: String?,
    val culture: String?,
    val born: String?,
    val titles: List<String>?,
    val aliases: List<String>?,
    val playedBy: List<String>?
)
