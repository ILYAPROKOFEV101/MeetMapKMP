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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
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
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageList(navController: NavController, chatViewModel: ChatViewModel, username: String, my_avatar: String, my_key: String) {

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
                MessageCard(navController , chatViewModel, message, my_key, painter, username)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))  // Пробел перед текстовым полем

        // Поле ввода сообщения, закрепленное внизу
        Material_text_filed(chatViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageCard(navController: NavController ,chatViewModel: ChatViewModel, message: Messages_Chat, my_key: String, my_avatar: Painter, username: String) {
    val My_message_color = if (isSystemInDarkTheme()) Color(0xFF315ff3) else Color(0xFF2315FF3)
    val Notmy_message_color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF)
    else Color(0xFF303133)
    val painter = rememberAsyncImagePainter(model = message.profilerIMG)
    val height by remember { mutableStateOf(60.dp) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isMyMessage = message.key == my_key
    var click by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(true) }
    val my_massege = if (isSystemInDarkTheme()) Color(0xFFFFFFFF)
    else  Color(0xFF1B1B1B)
    val notmy_massege = if (isSystemInDarkTheme()) Color(0xFF1B1B1B) else  Color(0xFFFFFFFF)




    // Определил шрифт для сообщений
    val font = FontFamily(
        Font(R.font.open_sans_semi_condensed_regular, FontWeight.Normal),
    )
    Column(
        modifier = Modifier
            .clickable { click = !click // Переключаем состояние при клике
                if (click) {
                    // Добавляем сообщение в список
                    if( chatViewModel.getSendToServer() != null){
                        navController.navigate("delete"){
                            launchSingleTop = true // Этот флаг предотвращает создание нескольких экранов
                        }
                    } else {
                        navController.navigate("Friend"){
                            launchSingleTop = true // Этот флаг предотвращает создание нескольких экранов
                        }
                    }
                    chatViewModel.addToSendToServer(message.messageId.toString())
                } else {
                    // Удаляем сообщение из списка
                    chatViewModel.removeFromSendToServer(message.messageId.toString())
                }
            } // Изменяем состояние при клике
            .background(
                if (click) Color(0x002A2A2A).copy(alpha = 0.1f) // Синий прозрачный цвет
                else Color.Transparent // Прозрачный цвет по умолчанию
            )
    )
    {
Row(modifier = Modifier)
{
    Box(
        modifier = Modifier
            .padding(end = 8.dp) // Отступ между Checkbox и текстом
    ) {
        if (click) { // Условие показа Checkbox
            Checkbox(
                modifier = Modifier.clip(RoundedCornerShape(80.dp)),
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
    ) {


        Card(
            modifier = Modifier
                .wrapContentWidth()
                .padding(
                    end = if (isMyMessage) 0.dp else screenWidth * 0.2f,
                    top = 2.dp,
                    start = if (isMyMessage) screenWidth * 0.2f else 0.dp,
                    bottom = 2.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isMyMessage) Color(0xFF315FF3) else
                    Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )
        {
            Row(modifier = Modifier)
            {
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
                        color = if (!isMyMessage) my_massege
                        else Color(
                            0xFFFFFFFF
                        ),
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .wrapContentHeight(),
                        contentAlignment = Alignment.CenterEnd
                    )
                    {

                        val timestamp = message.messageTime ?: 1730975442465L // UTC время
                        val userZoneId = ZoneId.systemDefault() // Часовая зона пользователя

                        // Преобразуем миллисекунды в Instant, затем в LocalDateTime
                        val localDateTime =
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), userZoneId)
                        val formattedTime =
                            localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

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
}
        if (message.imageUrls.isNotEmpty()) {
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
            ) {
                LazyRow {
                    items(message.imageUrls) { imageUrl ->
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("https://imlhstamcqwacpgldxsf.supabase.co/storage/v1/object/public/avatars/$imageUrl"),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

    }
    }





@Composable
fun Upbar(Img_url: String, name: String , lasttime: String,) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF315FF3))
    )
    {
        Box(
            modifier = Modifier
            .weight(0.1f)
            )
        {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_24px),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clickable {


                    }

            )
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = Img_url,
                    builder = {
                        precision(Precision.EXACT)
                    }
                ),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp)
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        // имя и последнее время посещения
        Column(
            modifier = Modifier
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

@Composable
fun DeleteMessage(rooid: String, navController: NavController, chatViewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF315FF3)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                // Действие для кнопки "Назад"
                 navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White
            )
        }
        IconButton(
            onClick = {
                chatViewModel.delete_from_local_db(rooid, chatViewModel.getSendToServer())
               chatViewModel.sendDeleteMessage(chatViewModel.getSendToServer())

            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить сообщение",
                tint = Color.White
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material_text_filed(chatViewModel: ChatViewModel) {
    val customFontFamily = FontFamily(
        Font(R.font.open_sans_semi_condensed_regular, FontWeight.Normal)
    )
    val messages by chatViewModel.messages.collectAsState()
    val My_message_color = if (isSystemInDarkTheme()) Color(0xFF315ff3) else Color(0xFF2315FF3)
    val Notmy_message_color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF2315FF3)
    val background_color = if (isSystemInDarkTheme()) Color(0xFF191C20) else Color(0xFFFFFFFF)
    val backgroundColor = if (isSystemInDarkTheme()) Color(0xFF191C20) else Color(0xFFFFFFFF)
    val cursorColor = if (isSystemInDarkTheme()) Color(0xFF191C20) else Color(0xFFFFFFFF)
    val textColor = if (isSystemInDarkTheme())  Color(0xFFFFFFFF) else  Color(0xFF191C20)

    val context = LocalContext.current // Получение текущего контекста
    var text by remember { mutableStateOf("") }

    val fileViewModel: FileViewModel = viewModel()
    val fileList by fileViewModel.fileList.observeAsState(emptyList())

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris?.let {
                Log.d("MaterialTextFiled", "Выбраны изображения: $uris")
                it.forEach { uri ->
                    val file = createTempFileFromUri(context, uri)
                    file?.let { tempFile ->
                        val uniqueFileName = "${UUID.randomUUID()}.png"
                        fileViewModel.addFile(tempFile, uniqueFileName)
                        Log.d("MaterialTextFiled", "Файл добавлен: $uniqueFileName")
                    } ?: Log.e("MaterialTextFiled", "Ошибка создания временного файла")
                }
            } ?: Log.e("MaterialTextFiled", "URI файлов не получены")
        }
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Отображение выбранных файлов
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fileList) { (file, filename) ->
                Box(modifier = Modifier.size(60.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(file),
                        contentDescription = filename,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                fileViewModel.removeFile(file) // Удаление файла
                            }
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Удалить",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .size(16.dp)
                            .clickable {
                                fileViewModel.removeFile(file) // Удаление файла
                            }
                    )
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)  // Позволяет Row увеличиваться по высоте
        ) {
            IconButton(
                modifier = Modifier
                    .weight(0.1f)
                    .align(Alignment.CenterVertically),
                onClick = {
                    // Открытие галереи для выбора изображений
                    Log.d("MaterialTextField", "Открываем галерею для выбора изображений")
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()  // Позволяет TextField расширяться по ширине
                    .wrapContentHeight(),  // Динамически увеличивает высоту по мере ввода текста
                textStyle = TextStyle(
                    fontFamily = customFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,    // Цвет фона
                    unfocusedContainerColor = backgroundColor,  // Цвет фона
                    focusedIndicatorColor = Color.Transparent,  // Прозрачный индикатор
                    unfocusedIndicatorColor = Color.Transparent, // Прозрачный индикатор
                    cursorColor = cursorColor,                  // Цвет курсора
                    focusedTextColor = textColor,               // Цвет текста при фокусе
                    unfocusedTextColor = textColor              // Цвет текста без фокуса
                ),
                maxLines = Int.MAX_VALUE,  // Убираем ограничение на количество строк
                minLines = 1  // Можно задать минимальное количество строк
            )

        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically),
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Получаем список файлов с именами
                        val filesWithNames = fileViewModel.getAllFileNames()

                        // Отправляем сообщение с именами файлов
                        chatViewModel.sendMessage(
                            content = text.toString(),
                            imageUrls = filesWithNames, // Имена файлов
                            videoUrls = emptyList(),
                            gifUrls = emptyList(),
                            fileUrls = emptyList()
                        )

                        // Очищаем текстовое поле
                        text = ""
                        Log.d("MaterialTextFiled", "Сообщение отправлено с файлами: ${filesWithNames}")
                    } catch (e: Exception) {
                        Log.e("MaterialTextFiled", "Ошибка при отправке сообщения: ${e.message}")
                    }
                }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Загружаем файлы в хранилище
                        val filesWithNames = fileViewModel.getFileandFileNmae()

                        if (filesWithNames.isNotEmpty()) {
                            fileViewModel.setUploadingState(true) // Устанавливаем состояние загрузки

                            filesWithNames.forEach { (file, fileName) ->
                                try {
                                    val success = bucketManager.createBucketAndUploadPhoto(
                                        bucketName = "avatars",
                                        fileName = fileName,
                                        file = file.readBytes(),
                                        log = androidLog("BucketManager")
                                    )
                                    if (success) {
                                        Log.d("MaterialTextFiled", "Загрузка успешна: $fileName")
                                    } else {
                                        Log.e("MaterialTextFiled", "Ошибка при загрузке файла: $fileName")
                                    }
                                } catch (e: Exception) {
                                    Log.e("MaterialTextFiled", "Ошибка при обработке файла $fileName: ${e.message}")
                                }
                            }
                        } else {
                            Log.e("MaterialTextFiled", "Нет файлов для загрузки")
                        }
                    } catch (e: Exception) {
                        Log.e("MaterialTextFiled", "Общая ошибка при загрузке файлов: ${e.message}")
                    } finally {
                        fileViewModel.setUploadingState(false)
                        fileViewModel.clearFileList() // Очищаем список файлов и имен после загрузки
                        Log.d("MaterialTextFiled", "Очистка завершена")
                    }
                }
            }


        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
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

