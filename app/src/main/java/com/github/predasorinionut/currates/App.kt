package com.github.predasorinionut.currates

import android.app.Activity
import android.app.Application
import com.github.predasorinionut.currates.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        AndroidThreeTen.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}