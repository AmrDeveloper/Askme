package com.amrdeveloper.askme.di

import androidx.lifecycle.ViewModel
import com.amrdeveloper.askme.viewmodels.LoginViewModel
import com.amrdeveloper.askme.viewmodels.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel) : ViewModel
}