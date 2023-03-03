package com.roblescode.chatgpt.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roblescode.chatgpt.data.ANSWER
import com.roblescode.chatgpt.data.ERROR
import com.roblescode.chatgpt.data.MESSAGE
import com.roblescode.chatgpt.data.OpenIAState
import com.roblescode.chatgpt.data.repository.OpenIARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OpenIAViewModel @Inject constructor(
    private val repo: OpenIARepository
) : ViewModel() {

    data class ChatItem(
        val text: String,
        val hour: String,
        val type: String
    )

    var answerResponse by mutableStateOf<OpenIAState<String>>(OpenIAState.Success(null))
        private set

    val chatList = mutableStateListOf<ChatItem>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(text: String) {
        chatList.add(ChatItem(text = text, type = MESSAGE, hour = getHour()))
        sendMessageToAPI(text)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessageToAPI(text: String) = viewModelScope.launch {
        answerResponse = OpenIAState.Loading
        answerResponse = repo.getAnswer(text)
        when (val response = answerResponse) {
            is OpenIAState.Loading -> {/*TODO*/}
            is OpenIAState.Success -> {
                response.data?.let {
                    chatList.add(
                        ChatItem(
                            text = it.trimStart(),
                            type = ANSWER,
                            hour = getHour(),
                        )
                    )
                }
            }
            is OpenIAState.Failure -> {
                chatList.add(
                    ChatItem(
                        text = "An unknown error occurred!",
                        type = ERROR,
                        hour = getHour()
                    )
                )
            }
        }
    }

    fun clearChat() {
        chatList.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHour(): String {
        val actualHour = LocalTime.now()
        return actualHour.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
    }

}