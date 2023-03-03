package com.roblescode.chatgpt.data.api

import com.roblescode.chatgpt.data.model.CompletionRequest
import com.roblescode.chatgpt.data.model.CompletionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface OpenIAService{
    @POST("/v1/engines/{engine}/completions")
    suspend fun getCompletion(
        @Path("engine") engine:String,
        @Body request: CompletionRequest
    ) : CompletionResponse
}