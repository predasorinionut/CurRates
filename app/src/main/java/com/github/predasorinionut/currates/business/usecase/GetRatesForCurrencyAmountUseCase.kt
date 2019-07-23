package com.github.predasorinionut.currates.business.usecase

import com.github.predasorinionut.currates.R
import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.business.repositories.RatesRepository
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyFlags
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyNames
import io.reactivex.Observable
import io.reactivex.Single
import java.math.MathContext
import javax.inject.Inject

class GetRatesForCurrencyAmountUseCase @Inject constructor(
    private val ratesRepository: RatesRepository,
    @ForCurrencyNames private val currencyNamesMap: Map<String, String>,
    @ForCurrencyFlags private val currencyFlagsMap: Map<String, Int>
) : (String, Double) -> Single<List<CurrencyModel>> {
    override fun invoke(baseCurrency: String, amount: Double): Single<List<CurrencyModel>> =
        Observable.concat(
            Observable.just(Pair(baseCurrency, amount.toString())),
            ratesRepository.getRatesByCurrency(baseCurrency)
                .flatMapObservable { Observable.fromIterable(it.rates.toList()) }
        )
            .map { pair ->
                val rate = amount.toBigDecimal().multiply(pair.second.toBigDecimal(), MathContext.DECIMAL64)

                return@map CurrencyModel(
                    pair.first,
                    currencyNamesMap[pair.first] ?: "Unknown code name",
                    rate,
                    currencyFlagsMap[pair.first] ?: R.drawable.flag_placeholder
                )
            }
            .toList()
}