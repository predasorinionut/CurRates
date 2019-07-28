package com.github.predasorinionut.currates.datasource.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.threeten.bp.LocalDate

@Entity(tableName = "rates", primaryKeys = ["basecurrencycode", "currencycode"])
data class Rate(
    @ColumnInfo(name = "basecurrencycode")
    val baseCode: String,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "currencycode")
    val code: String,
    @ColumnInfo(name = "currencyrate")
    val rate: Double
)