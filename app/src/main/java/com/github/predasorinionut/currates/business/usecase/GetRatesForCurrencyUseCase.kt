package com.github.predasorinionut.currates.business.usecase

import com.github.predasorinionut.currates.business.repositories.CurrenciesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRatesForCurrencyUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) : (String) -> Single<Map<String, String>> {
    override fun invoke(baseCurrency: String): Single<Map<String, String>> =
        currenciesRepository.getRatesForCurrency(baseCurrency)
}