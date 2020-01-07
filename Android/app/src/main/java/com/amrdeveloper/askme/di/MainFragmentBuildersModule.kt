package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.views.*
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


    @ContributesAndroidInjector
    abstract fun contributeAskQuestionFragment() : AskQuestionFragment

    @ContributesAndroidInjector
    abstract fun contributeAnswerQuestionFragment() : AnswerQuestionFragment

    @ContributesAndroidInjector
    abstract fun contributeQuestionAnswerFragment() : QuestionAnswerFragment
}