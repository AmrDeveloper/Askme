package com.amrdeveloper.askme.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AskmeClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SERVER_API_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getFeedService() : FeedService{
        return retrofit.create(FeedService::class.java)
    }

    fun getQuestionService() : QuestionService{
        return retrofit.create(QuestionService::class.java)
    }

    fun getAnswerService() : AnswerService{
        return retrofit.create(AnswerService::class.java)
    }

    fun getFollowService() : FollowService{
        return retrofit.create(FollowService::class.java)
    }

    fun getReactionService() : ReactionService{
        return retrofit.create(ReactionService::class.java)
    }

    fun getNotificationService(): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
}