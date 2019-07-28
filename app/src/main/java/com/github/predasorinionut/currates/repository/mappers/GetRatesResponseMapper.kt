package com.github.predasorinionut.currates.repository.mappers

import com.github.predasorinionut.currates.R
import com.github.predasorinionut.currates.business.models.CurrencyModel
import com.github.predasorinionut.currates.common.ModelMapper
import com.github.predasorinionut.currates.datasource.api.GetRatesResponse
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyFlags
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyNames
import java.math.MathContext
import javax.inject.Inject

class GetRatesResponseMapper @Inject constructor(
    @ForCurrencyNames private val currencyNamesMap: Map<String, String>,
    @ForCurrencyFlags private val currencyFlagsMap: Map<String, Int>
) : ModelMapper<GetRatesResponse, List<CurrencyModel>> {
    override fun map(input: GetRatesResponse): List<CurrencyModel> {
        val currencyModelList = mutableListOf<CurrencyModel>()

        currencyModelList.add(
            CurrencyModel(
                code = input.base,
                name = currencyNamesMap[input.base] ?: "Unknown code name",
                flagId = currencyFlagsMap[input.base] ?: R.drawable.flag_placeholder,
                amount = "0".toBigDecimal(MathContext.DECIMAL64)
            )
        )

        input.rates.forEach { (code, rate) ->
            currencyModelList.add(
                CurrencyModel(
                    code = code,
                    name = currencyNamesMap[code] ?: "Unknown code name",
                    flagId = currencyFlagsMap[code] ?: R.drawable.flag_placeholder,
                    amount = rate.toBigDecimal(MathContext.DECIMAL64)
                )
            )
        }

        return currencyModelList
    }
}