package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.data.SERVER_API_URL
import com.amrdeveloper.askme.data.source.*
import com.amrdeveloper.askme.data.source.remote.FeedRemoteDataSource
import com.amrdeveloper.askme.data.source.remote.NotificationRemoteDataSource
import com.amrdeveloper.askme.data.source.remote.UserRemoteDataSource
import com.amrdeveloper.askme.data.source.remote.net.*
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
object AppModule {

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
    fun provideFeedDataSource(feedService: FeedService) : FeedDataSource {
        return FeedRemoteDataSource(feedService)
    }

    @Singleton
    @Provides
    fun provideUserDataSource(userService: UserService) : UserDataSource {
        return UserRemoteDataSource(userService)
    }

    @Singleton
    @Provides
    fun provideNotificationDataSource(notificationService: NotificationService) : NotificationDataSource {
        return NotificationRemoteDataSource(notificationService)
    }

}