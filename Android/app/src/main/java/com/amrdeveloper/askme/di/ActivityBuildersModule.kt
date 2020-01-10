package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.views.SettingsActivity
import com.amrdeveloper.askme.views.LoginActivity
import com.amrdeveloper.askme.views.MainActivity
import com.amrdeveloper.askme.views.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthViewModelModule::class,
            AuthModule::class
        ]
    )
    abstract fun contributeLoginActivity(): LoginActivity

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthViewModelModule::class,
            AuthModule::class
        ]
    )
    abstract fun contributeRegisterActivity(): RegisterActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuildersModule::class,
            MainViewModelModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity


    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuildersModule::class,
            MainViewModelModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeSettingsActivity(): SettingsActivity
}