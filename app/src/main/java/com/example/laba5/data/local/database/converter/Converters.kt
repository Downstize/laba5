package com.example.laba5.data.local.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Конвертация List<String> в String для сохранения в базе
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return gson.toJson(list)
    }

    // Конвертация String обратно в List<String>
    @TypeConverter
    fun fromStringToList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
}