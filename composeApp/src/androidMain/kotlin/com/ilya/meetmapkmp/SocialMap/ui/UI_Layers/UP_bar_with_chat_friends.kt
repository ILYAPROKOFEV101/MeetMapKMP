package com.ilya.MeetingMap.SocialMap.ui.UI_Layers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
@Preview
fun UP_bar_with_chat_friends()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f)
        )
        {
            Image(
                painter = rememberAsyncImagePainter(""),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(90.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(0.7f)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            )
            {
                Text(text = "")
            }

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )
            {
                Text(text = "")
            }

        }

        Column(modifier = Modifier.fillMaxHeight().weight(0.1f)) {

        }
    }
}
