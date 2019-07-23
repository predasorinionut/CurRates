package com.github.predasorinionut.currates.di.modules

import com.github.predasorinionut.currates.di.scopes.ActivityScope
import com.github.predasorinionut.currates.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    internal abstract fun contributeMainActivity(): MainActivity
}