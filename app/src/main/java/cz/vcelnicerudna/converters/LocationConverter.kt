package cz.vcelnicerudna.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.vcelnicerudna.models.Location

class LocationConverter {
    @TypeConverter
    fun toJson(news: Array<Location>): String {
        val type = object : TypeToken<Array<Location>>() {}.type
        return Gson().toJson(news, type)
    }
    @TypeConverter
    fun fromJson(data: String): Array<Location> {
        val type = object : TypeToken<Array<Location>>() {}.type
        return Gson().fromJson(data, type)
    }
}