package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.data.remote.net.*
import com.amrdeveloper.askme.ui.adapter.FeedAdapter
import com.amrdeveloper.askme.ui.adapter.NotificationAdapter
import com.amrdeveloper.askme.ui.adapter.PeopleAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltAppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SERVER_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit) : AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit) : UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideFeedService(retrofit: Retrofit) : FeedService {
        return retrofit.create(FeedService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionService(retrofit: Retrofit) : QuestionService {
        return retrofit.create(QuestionService::class.java)
    }

    @Singleton
    @Provides
    fun provideAnswerService(retrofit: Retrofit) : AnswerService {
        return retrofit.create(AnswerService::class.java)
    }

    @Singleton
    @Provides
    fun provideFollowService(retrofit: Retrofit) : FollowService {
        return retrofit.create(FollowService::class.java)
    }

    @Singleton
    @Provides
    fun provideReactionService(retrofit: Retrofit) : ReactionService {
        return retrofit.create(ReactionService::class.java)
    }

    @Singleton
    @Provides
    fun provideNotificationService(retrofit: Retrofit) : NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Singleton
    @Provides
    fun provideFeedAdapter() : FeedAdapter {
        return FeedAdapter()
    }

    @Singleton
    @Provides
    fun providePeopleAdapter() : PeopleAdapter {
        return PeopleAdapter()
    }

    @Singleton
    @Provides
    fun provideNotificationAdapter() : NotificationAdapter {
        return NotificationAdapter()
    }
}