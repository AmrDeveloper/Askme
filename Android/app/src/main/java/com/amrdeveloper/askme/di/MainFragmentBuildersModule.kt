package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.views.HomeFragment
import com.amrdeveloper.askme.views.NotificationFragment
import com.amrdeveloper.askme.views.PeopleFragment
import com.amrdeveloper.askme.views.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment() : HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment() : NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributePeopleFragment() : PeopleFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment() : ProfileFragment
}