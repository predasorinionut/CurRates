package com.github.predasorinionut.currates.di.modules

import android.content.Context
import com.github.predasorinionut.currates.di.scopes.ActivityScope
import com.github.predasorinionut.currates.ui.BaseActivity
import dagger.Binds
import dagger.Module

@Module
interface BaseActivityBindingModule<ACTIVITY : BaseActivity> {
    @Binds
    fun bindBaseActivity(activity: ACTIVITY): BaseActivity

    @Binds
    @ActivityScope
    fun bindContext(activity: ACTIVITY): Context
}