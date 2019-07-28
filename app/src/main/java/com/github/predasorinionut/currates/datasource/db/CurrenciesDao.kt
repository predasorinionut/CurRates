package com.github.predasorinionut.currates.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrenciesDao {
    /**
     * Get a currency by code.
     * @return the currency from the table with a specific code.
     */
    @Query("SELECT * FROM currencies WHERE currencycode = :code")
    fun getCurrencyByCode(code: String): Single<Currency>

    /**
     * Get all currencies.
     * @return all the currencies from the table.
     */
    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): Single<List<Currency>>

    /**
     * Insert a currency in the database. If the currency already exists, replace it.
     * @param currency the currency to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency): Completable

    /**
     * Delete all currencies.
     */
    @Query("DELETE FROM currencies")
    fun deleteAllCurrencies()
}
