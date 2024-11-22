package com.ilya.MeetingMap.SocialMap.ui.UI_Layers

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage

import com.ilya.meetmapkmp.R
import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat

import com.ilya.meetmapkmp.SocialMap.ViewModel.ChatViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageList(chatViewModel: ChatViewModel, username: String, my_avatar: String, my_key: String) {



    val messages by chatViewModel.messages.collectAsState()
    val My_message_color = if (isSystemInDarkTheme()) Color(0xFF315ff3) else Color(0xFF2315FF3)
    val Notmy_message_color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF2315FF3)
    val background_color = if (isSystemInDarkTheme()) Color(0xFF191C20) else Color(0xFFFFFFFF)

    Log.d("MessageList", "Number of messages: ${messages.size}")
    val listState = rememberLazyListState()
    val hasScrolled = rememberSaveable { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(model = my_avatar)

    // LaunchedEffect для прокрутки к последнему сообщению
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            val lastVisibleItemIndex = messages.size - 1
            listState.animateScrollToItem(lastVisibleItemIndex)
            hasScrolled.value = true
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(background_color)
    ) {
        // Список сообщений
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),  // Расширяет LazyColumn, чтобы занять все доступное пространство
            reverseLayout = false,
            state = listState,
        ) {
            items(messages) { message ->
                Spacer(modifier = Modifier.height(10.dp))
                MessageCard(message, my_key, painter, username)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))  // Пробел перед текстовым полем

        // Поле ввода сообщения, закрепленное внизу
        Material_text_filed(chatViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageCard(message: Messages_Chat, my_key: String, my_avatar: Painter, username: String) {
    val My_message_color = if (isSystemInDarkTheme()) Color(0xFF315ff3) else Color(0xFF2315FF3)
    val Notmy_message_color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF)
    else Color(0xFF303133)
    val painter = rememberAsyncImagePainter(model = message.profilerIMG)
    val height by remember { mutableStateOf(60.dp) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isMyMessage = message.key == my_key

    // Определил шрифт для сообщений
    val font = FontFamily(
        Font(R.font.open_sans_semi_condensed_regular, FontWeight.Normal),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
    ) {
        val imageModifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(40.dp))


        if (!(isMyMessage)) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = imageModifier
                )
                Spacer(modifier = Modifier.width(2.dp))

        }

            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(
                        end = if (isMyMessage) 0.dp else screenWidth * 0.2f,
                        top = 2.dp,
                        start = if (isMyMessage) screenWidth * 0.2f else 0.dp,
                        bottom = 2.dp
                    ),
                colors = CardDefaults.cardColors(containerColor = if (isMyMessage) Color(0xFF315FF3) else Color(0xFFFFFFFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                )
                {
                    Text(
                        text = message.content.toString(),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        fontFamily = font,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isMyMessage) Color(0xFFFFFFFF) else Color(
                            0xFF1B1B1B
                        ),
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            // .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.CenterEnd
                    )
                    {

                        val timestamp = message.messageTime ?: 1730975442465L // UTC время
                        val userZoneId = ZoneId.systemDefault() // Часовая зона пользователя

                            // Преобразуем миллисекунды в Instant, затем в LocalDateTime
                        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), userZoneId)
                        val formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                            // Отображаем отформатированное время в Text
                        Text(
                            text = formattedTime,
                            fontSize = 12.sp,
                            color = if (isMyMessage) Color(0xFFFFFFFF) else Color(0xFF1B1B1B)
                        )


                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material_text_filed(chatViewModel: ChatViewModel) {
    val context = LocalContext.current // Получение текущего контекста
    var text by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    uploadToFirebase(it, context)
                }
            }
        }
    )

    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically),
            onClick = {
                // Запуск галереи
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                launcher.launch(intent)
            }
        ) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = "Send"
            )
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(0.7f)
                .height(80.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // Цвет контейнера при фокусе
                unfocusedContainerColor = Color.White, // Цвет контейнера без фокуса
                disabledContainerColor = Color.White, // Цвет контейнера, когда поле недоступно
                cursorColor = Color.White, // Цвет курсора
                focusedTextColor = Color.White, // Цвет текста при фокусе
                unfocusedTextColor = Color.White // Цвет текста без фокуса
            ),
            maxLines = 10
        )
        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically),
            onClick = {
                chatViewModel.sendMessage(
                    text.toString(),
                    gifUrls = emptyList(),
                    imageUrls = emptyList(),
                    videoUrls = emptyList(),
                    fileUrls = emptyList()
                )
                text = ""
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
}

fun uploadToFirebase(uri: Uri, context: Context) {
    val storageRef = FirebaseStorage.getInstance().reference
    val fileName = uri.lastPathSegment ?: "file_${System.currentTimeMillis()}"
    val fileRef = storageRef.child("uploads/$fileName")

    // Добавление логов
    Log.d("Upload", "Начало загрузки файла: $fileName")

    fileRef.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            Log.d("Upload", "Файл успешно загружен: ${taskSnapshot.metadata?.path}")
            fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                Log.d("Upload", "Ссылка на файл: $downloadUri")
                Toast.makeText(context, "Фото загружено!", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Upload", "Ошибка загрузки файла: ${exception.message}")
            Toast.makeText(context, "Ошибка загрузки: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
        .addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            Log.d("Upload", "Прогресс загрузки: $progress%")
        }
}

// Функция для проверки, является ли файл видео
private fun isVideoFile(context: Context, uri: Uri): Boolean {
    val contentResolver: ContentResolver = context.contentResolver
    val type = contentResolver.getType(uri)
    return type?.startsWith("video") == true
}


@Composable
fun VideoThumbnail(uri: Uri) {
    val context = LocalContext.current
    var thumbnail by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(uri) {
        thumbnail = withContext(Dispatchers.IO) {
            MediaStore.Video.Thumbnails.getThumbnail(
                context.contentResolver,
                uri.lastPathSegment?.toLongOrNull() ?: 0,
                MediaStore.Video.Thumbnails.MINI_KIND,
                null
            )
        }
    }

    thumbnail?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Video Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun Choos_type_of_file(){

    val tint = if (isSystemInDarkTheme()) Color(0xFFFFFFFF)
    else Color(0xFF191C20)
    val backgroundColor = if (isSystemInDarkTheme()) Color( 0xFF191C20)
    else Color(0xFFFFFFFF)
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(backgroundColor)
    ){
        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically), // Выравнивание по центру вертикально
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.UploadFile,
                contentDescription = "Send",
                tint = tint,
                modifier = Modifier.size(30.dp)
                )
        }
        Spacer(modifier = Modifier.widthIn(5.dp))

        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically), // Выравнивание по центру вертикально
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Send",
                tint = tint,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

