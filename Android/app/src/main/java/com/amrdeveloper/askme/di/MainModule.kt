package com.amrdeveloper.askme.di

import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.adapter.NotificationAdapter
import com.amrdeveloper.askme.adapter.PeopleAdapter
import com.amrdeveloper.askme.net.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideFeedAdapter() : FeedAdapter{
        return FeedAdapter()
    }

    @MainScope
    @Provides
    fun providePeopleAdapter() : PeopleAdapter{
        return PeopleAdapter()
    }

    @MainScope
    @Provides
    fun provideNotificationAdapter() : NotificationAdapter{
        return NotificationAdapter()
    }

    @MainScope
    @Provides
    fun provideUserService(retrofit: Retrofit) : UserService{
        return retrofit.create(UserService::class.java)
    }

    @MainScope
    @Provides
    fun provideFeedService(retrofit: Retrofit) : FeedService{
        return retrofit.create(FeedService::class.java)
    }

    @MainScope
    @Provides
    fun provideQuestionService(retrofit: Retrofit) : QuestionService {
        return retrofit.create(QuestionService::class.java)
    }

    @MainScope
    @Provides
    fun provideAnswerService(retrofit: Retrofit) : AnswerService {
        return retrofit.create(AnswerService::class.java)
    }

    @MainScope
    @Provides
    fun provideFollowService(retrofit: Retrofit) : FollowService {
        return retrofit.create(FollowService::class.java)
    }

    @MainScope
    @Provides
    fun provideReactionService(retrofit: Retrofit) : ReactionService {
        return retrofit.create(ReactionService::class.java)
    }

    @MainScope
    @Provides
    fun provideNotificationService(retrofit: Retrofit) : NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
}