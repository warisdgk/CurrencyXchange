package mwaris.dev.currencyxchange.data.local.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MapTypeConverter {

    @TypeConverter
    fun stringToMap(value: String): Map<String, Double>? {
        return Gson().fromJson(value, object : TypeToken<Map<String, Double>?>() {}.type)
    }

    @TypeConverter
    fun mapToString(value: Map<String, Double>?): String {
        return if (value == null) "" else Gson().toJson(value)
    }
}