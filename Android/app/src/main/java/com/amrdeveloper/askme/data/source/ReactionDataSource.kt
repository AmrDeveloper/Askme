package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.ReactionData
import retrofit2.Response

interface ReactionDataSource {

    suspend fun createAnswerReaction(token : String, reactionData: ReactionData) : Result<Response<String>>

    suspend fun deleteAnswerReaction(token : String, reactionData: ReactionData) : Result<Response<String>>
}