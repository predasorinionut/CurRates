package com.github.predasorinionut.currates.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.LocalDate

@Dao
interface RatesDao {
    /**
     * Get a rate by code.
     * @return the rate from the table with a specific code.
     */
    @Query("SELECT currencyrate FROM rates WHERE currencycode = :code")
    fun getRateByCode(code: String): Single<Double>

    /**
     * Get all rates.
     *
     * @param workaroundBaseCode has the same value as {@code baseCode}; it is used as a workaround
     * for a SQLite problem(if you use the same param multiple times in a query, it crashes)
     *
     * @return all the rates from the table.
     */
    @Query("SELECT * FROM rates WHERE basecurrencycode = :baseCode AND currencycode != :workaroundBaseCode AND date = :date")
    fun getAllRates(baseCode: String, workaroundBaseCode: String = baseCode, date: LocalDate = LocalDate.now()): Single<List<Rate>>

    /**
     * Insert a rate in the database. If the rate already exists, replace it.
     * @param rate the rate to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRate(rate: Rate): Completable

    /**
     * Delete all rates.
     */
    @Query("DELETE FROM rates")
    fun deleteAllRates()
}
