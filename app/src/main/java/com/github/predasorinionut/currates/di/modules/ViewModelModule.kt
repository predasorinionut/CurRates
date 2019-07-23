package com.github.predasorinionut.currates.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.predasorinionut.currates.di.ViewModelFactory
import com.github.predasorinionut.currates.di.qualifiers.ViewModelKey
import com.github.predasorinionut.currates.di.scopes.ApplicationScope
import com.github.predasorinionut.currates.vm.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
abstract class ViewModelModule {
    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideDIViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory =
            ViewModelFactory(providers)
    }

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(mainViewModel: MainViewModel): ViewModel
}