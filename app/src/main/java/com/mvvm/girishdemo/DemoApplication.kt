package com.mvvm.girishdemo

import android.app.Application
import com.mvvm.girishdemo.di.AppModule
import com.mvvm.girishdemo.di.DaggerAppComponent
import com.mvvm.girishdemo.di.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/25/2020.
 */
class DemoApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = activityInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(BuildConfig.URL))
            .build().inject(this)
    }
}