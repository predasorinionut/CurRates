package com.github.predasorinionut.currates.business.usecase

import com.github.predasorinionut.currates.business.repositories.RatesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRatesForCurrencyUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) : (String) -> Single<Map<String, String>> {
    override fun invoke(baseCurrency: String): Single<Map<String, String>> =
        ratesRepository.getRatesByCurrency(baseCurrency)
            .map { it.rates }
}