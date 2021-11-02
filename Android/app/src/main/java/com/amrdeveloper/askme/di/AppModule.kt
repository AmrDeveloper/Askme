package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.data.SERVER_API_URL
import com.amrdeveloper.askme.data.source.*
import com.amrdeveloper.askme.data.source.remote.*
import com.amrdeveloper.askme.data.source.remote.service.*
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
    fun provideAuthDataSource(authService: AuthService) : AuthDataSource {
        return AuthRemoteDataSource(authService)
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

    @Singleton
    @Provides
    fun provideQuestionDataSource(questionService: QuestionService) : QuestionDataSource {
        return QuestionRemoteDataSource(questionService)
    }

    @Singleton
    @Provides
    fun provideAnswerDataSource(answerService: AnswerService) : AnswerDataSource {
        return AnswerRemoteDataSource(answerService)
    }

    @Singleton
    @Provides
    fun provideReactionDataSource(reactionService: ReactionService) : ReactionDataSource {
        return ReactionRemoteDataSource(reactionService)
    }

    @Singleton
    @Provides
    fun provideFollowDataSource(followService: FollowService) : FollowDataSource {
        return FollowRemoteDataSource(followService)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authDataSource: AuthDataSource) : AuthRepository {
        return AuthRepository(authDataSource)
    }

    @Singleton
    @Provides
    fun provideAnswerRepository(answerDataSource: AnswerDataSource) : AnswerRepository {
        return AnswerRepository(answerDataSource)
    }

    @Singleton
    @Provides
    fun provideFeedRepository(feedDataSource: FeedDataSource) : FeedRepository {
        return FeedRepository(feedDataSource)
    }

    @Singleton
    @Provides
    fun provideFollowRepository(followDataSource: FollowDataSource) : FollowRepository {
        return FollowRepository(followDataSource)
    }

    @Singleton
    @Provides
    fun provideNotificationRepository(notificationDataSource: NotificationDataSource) : NotificationRepository {
        return NotificationRepository(notificationDataSource)
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(questionDataSource: QuestionDataSource) : QuestionRepository {
        return QuestionRepository(questionDataSource)
    }

    @Singleton
    @Provides
    fun provideReactionRepository(reactionDataSource: ReactionDataSource) : ReactionRepository {
        return ReactionRepository(reactionDataSource)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userDataSource: UserDataSource) : UserRepository {
        return UserRepository(userDataSource)
    }
}