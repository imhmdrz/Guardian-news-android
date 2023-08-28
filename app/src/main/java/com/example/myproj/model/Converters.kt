package com.example.myproj.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromThumbnailFields(fields: ThumbnailFields): String {
        return gson.toJson(fields)
    }

    @TypeConverter
    fun toThumbnailFields(fieldsString: String): ThumbnailFields {
        return gson.fromJson(fieldsString, ThumbnailFields::class.java)
    }
}