package com.github.predasorinionut.currates.business.entities

data class CurrencyRates(
    val base: String,
    val date: String,
    val rates: Map<String, String>
)