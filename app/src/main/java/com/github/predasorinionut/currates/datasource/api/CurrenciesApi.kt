package com.github.predasorinionut.currates.datasource.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {
    @GET("/latest")
    fun getRates(
        @Query("base") base: String
    ): Single<GetRatesResponse>
}