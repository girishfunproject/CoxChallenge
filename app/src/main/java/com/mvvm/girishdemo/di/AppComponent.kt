package com.mvvm.girishdemo.di

import com.mvvm.girishdemo.DemoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
@Singleton
@Component(
    modules = [AndroidInjectionModule::class, BuildersModule::class, AppModule::class, NetworkModule::class]
)
interface AppComponent {
    fun inject(app: DemoApplication)
}