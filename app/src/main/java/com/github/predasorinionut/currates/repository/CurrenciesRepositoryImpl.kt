package com.github.predasorinionut.currates.repository

import androidx.room.EmptyResultSetException
import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.business.repositories.CurrenciesRepository
import com.github.predasorinionut.currates.datasource.api.CurrenciesApi
import com.github.predasorinionut.currates.datasource.db.CurrenciesDao
import com.github.predasorinionut.currates.datasource.db.Currency
import com.github.predasorinionut.currates.datasource.db.Rate
import com.github.predasorinionut.currates.datasource.db.RatesDao
import com.github.predasorinionut.currates.repository.mappers.CurrencyMapper
import com.github.predasorinionut.currates.repository.mappers.GetRatesResponseMapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import javax.inject.Inject

private const val DEFAULT_BASE_CURRENCY = "EUR"

class CurrenciesRepositoryImpl @Inject constructor(
    private val currenciesApi: CurrenciesApi,
    private val getRatesResponseMapper: GetRatesResponseMapper,
    private val currenciesDao: CurrenciesDao,
    private val currencyMapper: CurrencyMapper,
    private val ratesDao: RatesDao
) : CurrenciesRepository {
    override fun getAllCurrencies(): Single<List<CurrencyModel>> =
        currenciesDao.getAllCurrencies()
            .map {
                if (it.isEmpty())
                    throw EmptyResultSetException("No currencies found.")
                else
                    currencyMapper.map(it)
            }
            .onErrorResumeNext {
                return@onErrorResumeNext currenciesApi.getRates(DEFAULT_BASE_CURRENCY)
                    .map(getRatesResponseMapper::map)
                    .map { models ->
                        Observable.fromIterable(models)
                            .concatMapCompletable { model ->
                                currenciesDao.insertCurrency(
                                    Currency(
                                        code = model.code,
                                        name = model.name,
                                        flagId = model.flagId
                                    )
                                )
                            }
                            .subscribeOn(Schedulers.io())
                            .subscribe()

                        models
                    }
            }
            .subscribeOn(Schedulers.io())

    override fun getRatesForCurrency(currencyCode: String): Single<Map<String, String>> =
        currenciesApi.getRates(currencyCode)
            .map {
                if (it.rates.isEmpty()) {
                    throw EmptyResultSetException("No rates at this time. Please try later.")
                } else {
                    Observable.fromIterable(it.rates.toList())
                        .concatMapCompletable { pair ->
                            ratesDao.insertRate(
                                Rate(
                                    baseCode = currencyCode,
                                    date = LocalDate.now(),
                                    code = pair.first,
                                    rate = pair.second.toDouble()
                                )
                            )
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()

                    it.rates
                }
            }
            .onErrorResumeNext {
                ratesDao.getAllRates(baseCode = currencyCode)
                    .map { rateList -> rateList.associateBy({ it.code }, { it.rate.toString() }) }
            }
            .subscribeOn(Schedulers.io())
}