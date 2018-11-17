package cz.vcelnicerudna.configuration

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.vcelnicerudna.models.News

class NewsConverter {
    @TypeConverter
    fun toJson(news: Array<News>): String {
        val type = object : TypeToken<Array<News>>() {}.type
        return Gson().toJson(news, type)
    }
    @TypeConverter
    fun fromJson(data: String): Array<News> {
        val type = object : TypeToken<Array<News>>() {}.type
        return Gson().fromJson(data, type)
    }
}