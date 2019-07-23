package com.github.predasorinionut.currates.di.qualifiers

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [(AnnotationTarget.FUNCTION)])
annotation class ViewModelKey(val value: KClass<out ViewModel>)