package com.ilya.meetmapkmp.SocialMap.ui.UI_Layers

import android.content.ContentResolver
import android.content.Context
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Precision

import com.ilya.meetmapkmp.R
import com.ilya.Supabase.androidLog
import com.ilya.Supabase.bucketManager

import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat

import com.ilya.meetmapkmp.SocialMap.ViewModel.ChatViewModel
import com.ilya.meetmapkmp.SocialMap.ViewModel.FileViewModel

import createTempFileFromUri

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun Upbar(
    modifier: Modifier,
    Img_url: String,
    name: String,
    lasttime: String
) {
    val coroutineScope = rememberCoroutineScope()
    var isNavReady by remember { mutableStateOf(false) }



    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFF315FF3))
    ) {
        Box(modifier = modifier.weight(0.1f)) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_24px),
                contentDescription = "Back",
                tint = Color.White,
                modifier = modifier
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clickable(
                        enabled = isNavReady,
                        onClick = {

                        }
                    )
            )
        }
        Spacer(modifier = modifier.fillMaxWidth(0.1f))

        Box(
            modifier = modifier
                .weight(0.3f)
                .fillMaxHeight()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Img_url)
                    .crossfade(true) // Плавная анимация загрузки
                    .placeholder(R.drawable.placeholder) // Placeholder при загрузке
                    .error(R.drawable.photo_error_icon) // Изображение при ошибке
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp) // Фиксированный размер
                    .clip(CircleShape) // Круглая форма

            )
        }
        Spacer(modifier = modifier.fillMaxWidth(0.1f))

        // имя и последнее время посещения
        Column(
            modifier = modifier
                .weight(0.5f)
                .fillMaxHeight()){
            Text(
                text = name,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.open_sans_semi_condensed_regular)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Text(
                text = lasttime,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.open_sans_semi_condensed_regular)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        }
    }
}