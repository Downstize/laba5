package com.example.laba5.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.laba5.data.local.dao.CharacterDao
import com.example.laba5.data.local.database.converter.Converters

@Database(entities = [CharacterEntity::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}