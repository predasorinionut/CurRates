package com.github.predasorinionut.currates.repository.mappers

import com.github.predasorinionut.currates.business.entities.CurrencyRates
import com.github.predasorinionut.currates.common.ModelMapper
import com.github.predasorinionut.currates.datasource.GetRatesResponse
import javax.inject.Inject

class CurrencyRatesMapper @Inject constructor() : ModelMapper<GetRatesResponse, CurrencyRates> {
    override fun map(input: GetRatesResponse): CurrencyRates = input.run {
        CurrencyRates(
            base = base,
            date = date,
            rates = rates
        )
    }
}