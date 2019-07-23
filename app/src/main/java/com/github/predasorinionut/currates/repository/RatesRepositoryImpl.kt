package com.github.predasorinionut.currates.repository

import com.github.predasorinionut.currates.business.entities.CurrencyRates
import com.github.predasorinionut.currates.business.repositories.RatesRepository
import com.github.predasorinionut.currates.datasource.RatesApi
import com.github.predasorinionut.currates.repository.mappers.CurrencyRatesMapper
import io.reactivex.Single
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val ratesApi: RatesApi,
    private val rateMapper: CurrencyRatesMapper
) : RatesRepository {
    override fun getRatesByCurrency(currencyCode: String): Single<CurrencyRates> =
        ratesApi.getRates(currencyCode)
            .map(rateMapper::map)
}