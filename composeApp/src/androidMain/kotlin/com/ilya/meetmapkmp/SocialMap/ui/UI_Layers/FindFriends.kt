package com.ilya.MeetingMap.SocialMap.ui.UI_Layers

import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.example.yourapp.ui.Find_friends_fragment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FindFriends() {
    var expanded by remember { mutableStateOf(false) }
    var tapCount by remember { mutableStateOf(0) }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .fillMaxSize()

            .clip(RoundedCornerShape(20.dp)),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Цвет контейнера карты
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                // Фрагмент или его содержимое

            }
            if (!expanded) {
                IconButton(
                    onClick = { expanded =  !expanded},
                    modifier = Modifier.align(Alignment.End) // Иконка в правом верхнем углу
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Открыть",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer, // Цвет иконки
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { expanded = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer, // Цвет иконки
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}