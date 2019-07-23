package com.github.predasorinionut.currates.datasource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetRatesResponse(
    @JsonProperty("base") val base: String,
    @JsonProperty("date") val date: String,
    @JsonProperty("rates") val rates: Map<String, String>
)