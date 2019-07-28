package com.github.predasorinionut.currates.business.models

import java.math.BigDecimal

data class CurrencyModel(
    val code: String,
    val name: String,
    val flagId: Int,
    var amount: BigDecimal
)