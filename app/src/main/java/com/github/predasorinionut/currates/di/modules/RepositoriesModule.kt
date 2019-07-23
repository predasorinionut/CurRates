package com.github.predasorinionut.currates.di.modules

import com.github.predasorinionut.currates.business.repositories.RatesRepository
import com.github.predasorinionut.currates.repository.RatesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoriesModule {
    @Binds
    abstract fun bindRatesRepositoryImpl(impl: RatesRepositoryImpl): RatesRepository
}