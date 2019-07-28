package com.github.predasorinionut.currates.di.modules

import com.github.predasorinionut.currates.business.repositories.CurrenciesRepository
import com.github.predasorinionut.currates.repository.CurrenciesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoriesModule {
    @Binds
    abstract fun bindCurrenciesRepositoryImpl(impl: CurrenciesRepositoryImpl): CurrenciesRepository
}