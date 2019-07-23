package com.github.predasorinionut.currates.vm

import androidx.lifecycle.ViewModel
import com.github.predasorinionut.currates.business.usecase.GetRatesForCurrencyAmountUseCase
import com.github.predasorinionut.currates.business.usecase.GetRatesForCurrencyUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getRatesForCurrencyAmountUseCase: GetRatesForCurrencyAmountUseCase,
    private val getRatesForCurrencyUseCase: GetRatesForCurrencyUseCase
) : ViewModel() {
    var rates = mutableMapOf<String, String>()

    fun getCurrencyModels(
        currency: String,
        amount: Double = 0.0
    ): Single<UiStateModel> =
        getRatesForCurrencyAmountUseCase(currency, amount)
            .map { UiStateModel.success(it) }
            .onErrorReturn { error -> UiStateModel.error(error) }
            .subscribeOn(Schedulers.io())

    fun updateRates(baseCurrency: String, intervalMillis: Long = 1000L): Flowable<Unit> =
        Flowable.interval(intervalMillis, TimeUnit.MILLISECONDS, Schedulers.io())
            .onBackpressureDrop()
            .concatMapSingle { getRatesForCurrencyUseCase(baseCurrency) }
            .onErrorResumeNext { s: Subscriber<in Map<String, String>> -> Observable.empty<Map<String, String>>() }
            .map { rates = it.toMutableMap() }
            .subscribeOn(Schedulers.io())
}