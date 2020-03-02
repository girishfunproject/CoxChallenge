package com.mvvm.girishdemo.di

import com.mvvm.girishdemo.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
@Module
abstract class BuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}