package cz.vcelnicerudna.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.vcelnicerudna.models.Price

class PricesConverter {
    @TypeConverter
    fun toJson(news: Array<Price>): String {
        val type = object : TypeToken<Array<Price>>() {}.type
        return Gson().toJson(news, type)
    }
    @TypeConverter
    fun fromJson(data: String): Array<Price> {
        val type = object : TypeToken<Array<Price>>() {}.type
        return Gson().fromJson(data, type)
    }
}