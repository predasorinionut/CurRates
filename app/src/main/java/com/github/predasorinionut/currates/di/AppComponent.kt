package com.github.predasorinionut.currates.di

import com.github.predasorinionut.currates.App
import com.github.predasorinionut.currates.di.modules.ActivityModule
import com.github.predasorinionut.currates.di.modules.AppModule
import com.github.predasorinionut.currates.di.modules.RepositoriesModule
import com.github.predasorinionut.currates.di.modules.ViewModelModule
import com.github.predasorinionut.currates.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        RepositoriesModule::class,
        AndroidInjectionModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}