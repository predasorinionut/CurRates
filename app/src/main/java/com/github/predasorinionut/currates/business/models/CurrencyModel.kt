package com.github.predasorinionut.currates.business.models

import java.math.BigDecimal

data class CurrencyModel(
    val code: String,
    val name: String,
    var rate: BigDecimal,
    val flagId: Int
)