package com.roblescode.chatgpt.data.repository


import android.util.Log
import com.roblescode.chatgpt.data.OpenIAState
import com.roblescode.chatgpt.data.api.OpenIAService
import com.roblescode.chatgpt.data.model.CompletionRequest
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OpenIARepository @Inject constructor(
    private val openIA: OpenIAService
) {
    suspend fun getAnswer(prompt: String): OpenIAState<String> {
        return try {
            val response = openIA.getCompletion(
                engine = "text-davinci-003",
                request = CompletionRequest(
                    prompt = prompt,
                    temperature = 0.0,
                    max_tokens = 100,
                    top_p = 1,
                    frequency_penalty = 0.0,
                    presence_penalty = 0.0,
                    stop = listOf("_")
                )
            )
            Log.d("LOG", response.choices.toString())
            OpenIAState.Success(response.choices[0].text)
        } catch (e: Exception) {
            OpenIAState.Failure(e)
        }
    }
}