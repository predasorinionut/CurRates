package com.github.predasorinionut.currates.datasource

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {
    @GET("/latest")
    fun getRates(
        @Query("base") base: String
    ): Single<GetRatesResponse>
}