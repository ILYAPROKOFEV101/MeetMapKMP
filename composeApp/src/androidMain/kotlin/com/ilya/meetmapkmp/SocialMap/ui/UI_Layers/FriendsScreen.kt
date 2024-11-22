package com.ilya.MeetingMap.SocialMap.ui.UI_Layers

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.api.Context
import com.ilya.MeetingMap.SocialMap.DataModel.Friend
import com.ilya.MeetingMap.SocialMap.Interface.MyDataProvider


import com.ilya.MeetingMap.SocialMap.ViewModel.FriendsViewModel


@Composable
fun FriendsScreen(friendsList: List<Friend>, navController: NavController, context: android.content.Context) {
    val backgroundColor = if (isSystemInDarkTheme()) Color.Black else colorScheme.primaryContainer
    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        ) {
        items(friendsList) { friend ->
            FriendItem(friend, navController, context)
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
fun FriendItem(friend: Friend, navController: NavController, context: android.content.Context) {


    // Получаем текущую цветовую схему
    val colorScheme = MaterialTheme.colorScheme

    // Определяем цвета для текста и фона в зависимости от темы
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

                // Переход к чату
                navController.navigate("Chat")
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RectangleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            AsyncImage(
                model = friend.img,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .width(60.dp)
                    .height(60.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = friend.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = friend.lastmessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
