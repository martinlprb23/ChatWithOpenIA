package com.roblescode.chatgpt.data.model

data class CompletionRequest(
    val prompt: String,
    val temperature: Double,
    val max_tokens: Int,
    val top_p: Int,
    val frequency_penalty: Double,
    val presence_penalty: Double,
    val stop: List<String>
)
