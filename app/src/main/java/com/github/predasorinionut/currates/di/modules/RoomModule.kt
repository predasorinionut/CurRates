package com.github.predasorinionut.currates.di.modules

import android.content.Context
import com.github.predasorinionut.currates.datasource.db.CurrenciesDao
import com.github.predasorinionut.currates.datasource.db.CurrenciesDatabase
import com.github.predasorinionut.currates.datasource.db.RatesDao
import com.github.predasorinionut.currates.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
abstract class RoomModule {
    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideCurrencyDatabase(context: Context): CurrenciesDatabase = CurrenciesDatabase.getInstance(context)

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideCurrenciesDao(database: CurrenciesDatabase): CurrenciesDao = database.currenciesDao()

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideRatesDao(database: CurrenciesDatabase): RatesDao = database.ratesDao()
    }
}