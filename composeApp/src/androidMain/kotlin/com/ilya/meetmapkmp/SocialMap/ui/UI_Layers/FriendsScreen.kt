package com.ilya.meetmapkmp.SocialMap.ui.UI_Layers

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.api.Context
import com.ilya.meetmapkmp.R
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend
import com.ilya.meetmapkmp.SocialMap.Interface.MyDataProvider
import com.ilya.meetmapkmp.SocialMap.ViewModel.WebSocket_getfriendsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun FriendsScreen(WebSocket_getfriendsViewModel : WebSocket_getfriendsViewModel, navController: NavController, context: android.content.Context) {
    val backgroundColor = if (isSystemInDarkTheme()) Color.Black else colorScheme.primaryContainer

    val friends by WebSocket_getfriendsViewModel.friends.collectAsState()



    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        ) {
        items(friends) { friend ->
            FriendItem(friend, navController, context, WebSocket_getfriendsViewModel)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 70.dp)
                    .background(backgroundColor)
            )
        }
    }
}

@Composable
fun FriendItem(
    friend: Friend,
    navController: NavController,
    context: android.content.Context,
    WebSocket_getfriendsViewModel: WebSocket_getfriendsViewModel
) {
    // Получаем текущую цветовую схему
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = if (isSystemInDarkTheme()) colorScheme.surface else Color.White
    val textColor = if (isSystemInDarkTheme()) colorScheme.onSurface else Color.Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                Log.d("Save_token", "сохраняю токен: ${friend.token}")
                val dataProvider = MyDataProvider(context)
                dataProvider.saveToken(friend.token) // Store the token
                navController.navigate("Chat")
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RectangleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(friend.img)
                    .crossfade(true) // Плавная анимация загрузки
                    .placeholder(R.drawable.placeholder) // Placeholder при загрузке
                    .error(R.drawable.photo_error_icon) // Изображение при ошибке
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp) // Фиксированный размер
                    .clip(CircleShape) // Круглая форма
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = friend.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = friend.lastmessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        WebSocket_getfriendsViewModel.deletefriends_from_bd(friend.token)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить друга",
                    tint = Color.Red // Цвет иконки удаления
                )
            }
        }
    }
}
