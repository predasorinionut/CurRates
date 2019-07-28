package com.github.predasorinionut.currates.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Currency::class, Rate::class], version = 1)
@TypeConverters(Converters::class)
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao
    abstract fun ratesDao(): RatesDao

    companion object {

        @Volatile
        private var INSTANCE: CurrenciesDatabase? = null

        fun getInstance(context: Context): CurrenciesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    ).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                CurrenciesDatabase::class.java, "Currencies.db"
            )
                .build()
    }
}