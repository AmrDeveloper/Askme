package com.amrdeveloper.askme.di

import androidx.lifecycle.ViewModel
import com.amrdeveloper.askme.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(notificationViewModel: NotificationViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionViewModel::class)
    abstract fun bindQuestionViewModel(questionViewModel: QuestionViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnswerQuestionViewModel::class)
    abstract fun bindAnswerViewModel(answerQuestionViewModel: AnswerQuestionViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionAnswerViewModel::class)
    abstract fun bindQuestionAnswerViewModel(questionAnswerViewModel: QuestionAnswerViewModel) : ViewModel
}