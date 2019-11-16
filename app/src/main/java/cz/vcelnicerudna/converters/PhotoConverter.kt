package cz.vcelnicerudna.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.vcelnicerudna.models.Photo

class PhotoConverter {
    @TypeConverter
    fun toJson(news: Array<Photo>): String {
        val type = object : TypeToken<Array<Photo>>() {}.type
        return Gson().toJson(news, type)
    }
    @TypeConverter
    fun fromJson(data: String): Array<Photo> {
        val type = object : TypeToken<Array<Photo>>() {}.type
        return Gson().fromJson(data, type)
    }
}