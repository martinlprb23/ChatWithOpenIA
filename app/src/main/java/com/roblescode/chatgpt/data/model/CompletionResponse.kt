package com.roblescode.chatgpt.data.model

data class CompletionResponse(
    val id: String,
    val created: Long,
    val model: String,
    val choices: List<CompletionChoice>
)
