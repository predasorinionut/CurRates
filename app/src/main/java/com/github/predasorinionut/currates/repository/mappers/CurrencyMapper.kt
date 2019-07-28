package com.github.predasorinionut.currates.repository.mappers

import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.common.ModelMapper
import com.github.predasorinionut.currates.datasource.db.Currency
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyMapper @Inject constructor() : ModelMapper<List<Currency>, List<CurrencyModel>> {
    override fun map(input: List<Currency>): List<CurrencyModel> {
        val currencyModelList = mutableListOf<CurrencyModel>()

        input.forEach {
            currencyModelList.add(
                CurrencyModel(
                    code = it.code,
                    name = it.name,
                    flagId = it.flagId,
                    amount = BigDecimal.ZERO
                )
            )
        }

        return currencyModelList
    }
}