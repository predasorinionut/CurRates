package com.github.predasorinionut.currates.di.modules

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.predasorinionut.currates.App
import com.github.predasorinionut.currates.BuildConfig
import com.github.predasorinionut.currates.datasource.RatesApi
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyFlags
import com.github.predasorinionut.currates.di.qualifiers.ForCurrencyNames
import com.github.predasorinionut.currates.di.scopes.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

@Module
abstract class AppModule {
    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideObjectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideApiRetrofit(jacksonObjectMapper: ObjectMapper): Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideRatesApi(retrofit: Retrofit): RatesApi = retrofit.create(RatesApi::class.java)

        @Provides
        @ApplicationScope
        @ForCurrencyNames
        @JvmStatic
        fun provideCurrencyNamesMap(context: Context, jacksonObjectMapper: ObjectMapper): Map<String, String> =
            jacksonObjectMapper.readValue(context.assets.open("currency_codes_to_names.json"))

        @Provides
        @ApplicationScope
        @ForCurrencyFlags
        @JvmStatic
        fun provideCurrencyFlagsMap(context: Context, @ForCurrencyNames currencyNamesMap: Map<String, String>): Map<String, Int> {
            val currencyFlagsMap: MutableMap<String, Int> = mutableMapOf()
            currencyNamesMap.iterator().forEach { entry ->
                currencyFlagsMap[entry.key] = context.resources.getIdentifier(
                    "flag_${entry.key.toLowerCase()}",
                    "drawable",
                    context.packageName
                )
            }

            return currencyFlagsMap.toMap()
        }
    }

    @ApplicationScope
    @Binds
    abstract fun bindApplicationContext(app: App): Context
}