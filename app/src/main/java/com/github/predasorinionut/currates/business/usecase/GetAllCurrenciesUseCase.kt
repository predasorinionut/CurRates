package com.github.predasorinionut.currates.business.usecase

import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.business.repositories.CurrenciesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) : () -> Single<List<CurrencyModel>> {
    override fun invoke(): Single<List<CurrencyModel>> =
        currenciesRepository.getAllCurrencies()
}