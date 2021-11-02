package com.amrdeveloper.askme.data.source.remote

import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.ReactionDataSource
import com.amrdeveloper.askme.data.source.remote.service.ReactionService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class ReactionRemoteDataSource @Inject constructor(
    private val reactionService: ReactionService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReactionDataSource {

    override suspend fun createAnswerReaction(token: String, reactionData: ReactionData): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(reactionService.createAnswerReaction(token, reactionData))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAnswerReaction(token: String, reactionData: ReactionData): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(reactionService.deleteAnswerReaction(token, reactionData))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}