package com.github.predasorinionut.currates.business.repositories

import com.github.predasorinionut.currates.business.entities.CurrencyRates
import io.reactivex.Single

interface RatesRepository {
    /**
     *  Returns all the rates, related to the passed currency code
     */
    fun getRatesByCurrency(currencyCode: String): Single<CurrencyRates>
}