package com.github.predasorinionut.currates.business.repositories

import com.github.predasorinionut.currates.business.models.CurrencyModel
import io.reactivex.Single

interface CurrenciesRepository {
    /**
     *  @returns a list of all the currencies
     */
    fun getAllCurrencies(): Single<List<CurrencyModel>>

    /**
     *  @returns all the rates, related to the passed currency code
     */
    fun getRatesForCurrency(currencyCode: String): Single<Map<String, String>>
}