package com.roblescode.chatgpt.data.model


data class CompletionChoice(
    val text: String,
    val index: Int,
    val logprobs: Map<String, List<Double>>,
    val finish_reason: String
)