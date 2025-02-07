package com.ilya.meetmapkmp.SocialMap.ui.UI_Layers

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.ilya.meetmapkmp.R


@Composable
fun Loop(navController: NavController) {
    val context = LocalContext.current
    val robotoBold = FontFamily(
        Font(R.font.roboto_medium) // Убедитесь, что имя файла правильное и соответствует переименованному файлу
    )
    val borderColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val backgroundColor = if (isSystemInDarkTheme()) Color(0xFF262627) else Color(0xFF0088CC)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {

            }
            .clip(RoundedCornerShape(0.dp))
        ,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 40.dp)
                    .weight(0.8f),
                contentAlignment = Alignment.CenterStart // Центровка содержимого внутри Box
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Start,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = robotoBold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f),
                contentAlignment = Alignment.Center // Центровка содержимого внутри Box
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.navigate("Friendsearch")
                        },
                    tint = Color.White
                )


            }
        }
    }
}
