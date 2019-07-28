package com.github.predasorinionut.currates.di

import com.github.predasorinionut.currates.App
import com.github.predasorinionut.currates.di.modules.*
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
        RoomModule::class,
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