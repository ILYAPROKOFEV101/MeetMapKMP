package com.ilya.MeetingMap.SocialMap.ui.UI_Layers
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ilya.MeetingMap.SocialMap.DataModel.Messages
import com.ilya.MeetingMap.SocialMap.ViewModel.ChatViewModel

@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {
    val messages by chatViewModel.messages.collectAsState()
     val TAG = "ChatWebSocketService"

        Log.d(TAG, "Received messages: $messages")
    Column {
        // Отображаем список сообщений
        LazyColumn(modifier = Modifier.fillMaxSize(0.8f)) {
            items(messages) { message ->
                MessageItem(message = message)
            }
        }

        // Поле для ввода и отправки сообщения
        var text by remember { mutableStateOf("") }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение") }
            )
            Button(onClick = {
                chatViewModel.sendMessage(text, gifUrls = emptyList(), imageUrls = emptyList(), videoUrls = emptyList(), fileUrls = emptyList())
                text = "" // Очищаем поле после отправки
            }) {
                Text("Отправить")
            }
        }
    }
}

@Composable
fun MessageItem(message: Messages) {
    Column {
        Text(text = "${message.senderUsername}: ${message.content}")
        // Если нужно, добавьте изображение или другие элементы
    }
}
