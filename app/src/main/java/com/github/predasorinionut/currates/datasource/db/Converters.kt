package com.github.predasorinionut.currates.datasource.db

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

class Converters {
    @TypeConverter
    fun textToLocalDate(text: String): LocalDate {
        return LocalDate.parse(text)
    }

    @TypeConverter
    fun localDateToText(date: LocalDate): String {
        return date.toString()
    }
}
