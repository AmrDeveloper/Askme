package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.net.AuthService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @MainScope
    @Provides
    fun provideUAuthService(retrofit: Retrofit) : AuthService {
        return retrofit.create(AuthService::class.java)
    }
}

