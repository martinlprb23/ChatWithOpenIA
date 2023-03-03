package com.roblescode.chatgpt.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roblescode.chatgpt.data.ANSWER
import com.roblescode.chatgpt.data.MESSAGE
import com.roblescode.chatgpt.data.OpenIAState
import com.roblescode.chatgpt.ui.theme.ChatAnswerColor
import com.roblescode.chatgpt.ui.theme.ChatBackgroundColor
import com.roblescode.chatgpt.ui.theme.ChatColor
import com.roblescode.chatgpt.ui.theme.InputColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: OpenIAViewModel = hiltViewModel()) {

    val text = rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "Chat with OpenIA")
        }, actions = {
            IconButton(onClick = { viewModel.clearChat() }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        }
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                viewModel.chatList.forEachIndexed { index, data ->
                    item(index) {
                        ContentMessage(data)
                    }
                }

                if (viewModel.answerResponse is OpenIAState.Loading) {
                    item {
                        Text(text = "...")
                    }
                }
            }
        }
    }, bottomBar = {
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 16.dp, end = 24.dp)
                .clip(RoundedCornerShape(50)),
            placeholder = { Text(text = "Message...") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.sendMessage(text = text.value)
                    text.value = ""
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = InputColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
        )
    }, containerColor = ChatBackgroundColor)
}

@Composable
fun ContentMessage(data: OpenIAViewModel.ChatItem) {
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = when (data.type) {
            MESSAGE -> Alignment.CenterEnd
            ANSWER -> Alignment.CenterStart
            else -> Alignment.Center
        }
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    when (data.type) {
                        MESSAGE -> ChatColor
                        ANSWER -> ChatAnswerColor
                        else -> Color.Blue
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = data.text, color = Color.White)
                Text(text = data.hour, fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }

}
