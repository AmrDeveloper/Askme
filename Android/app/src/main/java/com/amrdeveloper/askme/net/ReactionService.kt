package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.ReactionData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface ReactionService {

    @POST("reacts/")
    fun createAnswerReaction(
        @Header("authorization") token : String,
        @Body reactionData: ReactionData
    ) : Call<String>

    @DELETE("reacts/")
    fun deleteAnswerReaction(
        @Header("authorization") token : String,
        @Body reactionData: ReactionData
    ) : Call<String>
}