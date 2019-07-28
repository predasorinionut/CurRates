package com.github.predasorinionut.currates.datasource.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey
    @ColumnInfo(name = "currencycode")
    val code: String,
    @ColumnInfo(name = "currencyname")
    val name: String,
    @ColumnInfo(name = "flagid")
    val flagId: Int
)