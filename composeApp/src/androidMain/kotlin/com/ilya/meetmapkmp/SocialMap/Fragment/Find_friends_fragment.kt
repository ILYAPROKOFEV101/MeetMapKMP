package com.example.yourapp.ui




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.identity.Identity


import com.ilya.meetmapkmp.SocialMap.ui.theme.SocialMap
import com.ilya.codewithfriends.presentation.profile.ID
import com.ilya.codewithfriends.presentation.sign_in.GoogleAuthUiClient
import com.ilya.reaction.logik.PreferenceHelper.getUserKey

import coil.compose.rememberAsyncImagePainter
import com.ilya.meetmapkmp.Map.Server_API.POST.addFriends
import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends

import com.ilya.meetmapkmp.R
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend

import com.ilya.meetmapkmp.SocialMap.ViewModel.WebSocketViewModel
import com.ilya.meetmapkmp.SocialMap.ui.theme.robotomedium
import kotlinx.coroutines.*
import postRequestAddFriends


class Find_friends_fragment : Fragment() {


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext().applicationContext,
            oneTapClient = Identity.getSignInClient(requireContext().applicationContext)
        )
    }


    private val WebSocketViewModel: WebSocketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {

            setContent {
                SocialMap {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            // Убираем weight у Box
                            Box(
                                Modifier
                                    .height(150.dp) // Занимает только необходимое место
                                    .fillMaxWidth()
                            ) {
                                SearchBar(WebSocketViewModel) // Поисковая строка
                            }
                     // Собираем friendsList из StateFlow
                            val friends by WebSocketViewModel.friendsList.collectAsState()


                                // Отображаем список друзей
                                FriendsList(friends = friends)


                        }
                    }
                }
            }
            
        }

    }





    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @Composable
    fun SearchBar(webSocketViewModel: WebSocketViewModel) {
        val keyboardController = LocalSoftwareKeyboardController.current
        var name by remember { mutableStateOf("") } // Текущее значение ввода
        var lastSentName by remember { mutableStateOf("") } // Последнее отправленное значение

        SocialMap {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 30.dp, end = 30.dp),
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.write_user_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(
                            border = BorderStroke(
                                3.dp,
                                SolidColor(MaterialTheme.colorScheme.onSurface)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    value = name,
                    onValueChange = { newValue ->
                        name = newValue // Обновляем состояние при изменении
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide() // Скрываем клавиатуру
                            if (name.isNotEmpty() && name != lastSentName) {
                                webSocketViewModel.sendCommand("findFriends $name") // Отправляем команду
                                lastSentName = name // Обновляем последнее отправленное значение
                            }
                        }
                    ),
                )
            }

            // Автоматическая отправка каждые 3 секунды, если поле не пустое и значение изменилось
            LaunchedEffect(key1 = name) {
                while (isActive) {
                    delay(3000) // Ждем 3 секунды
                    if (name.isNotEmpty() && name != lastSentName) {
                        webSocketViewModel.sendCommand("findFriends $name") // Отправляем команду
                        lastSentName = name // Обновляем последнее отправленное значение
                    }
                }
            }
        }
    }

    @Composable
    fun FriendsList(friends: List<FindFriends>) {
        val key = getUserKey(requireContext())
        SocialMap {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background) // Цвет зависит от темы
                    .padding(start = 30.dp, end = 30.dp),
            )
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                items(friends) { friend ->
                    if (friend.key == key) return@items
                    FriendItem(friend)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }


    @Composable
    fun FriendItem(friend: FindFriends) {
        val uid = ID(
            userData = googleAuthUiClient.getSignedInUser()
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .height(100.dp)
            //.padding(8.dp)
        ) {
            // Аватар
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface) // Цвет фона поверхности
            ) {
                Image(
                    painter = rememberAsyncImagePainter(friend.img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(90.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                // Имя друга
                Text(
                    text = friend.name,
                    fontFamily = robotomedium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface // Цвет текста
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Кнопка
                Button(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentHeight(),
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {

                            val token = postRequestAddFriends(uid.toString(), key = getUserKey(requireContext()).toString(), friendKey = friend.key)
                            val data = Friend(
                                key = friend.key,
                                name = friend.name,
                                img = friend.img,
                                token = token.toString(),
                                lastmessage = "",
                                online = false
                            )
                            //   friendsRepository.insertOrUpdateFriend(data)
                                WebSocketViewModel.addfriends_to_bd(data)
                            addFriends(uid.toString(), key = getUserKey(requireContext()).toString(), friendKey = friend.key)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary, // Цвет кнопки
                        contentColor = MaterialTheme.colorScheme.onPrimary // Цвет текста на кнопке
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.addfriends),
                        fontFamily = robotomedium,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val uid = ID(userData = googleAuthUiClient.getSignedInUser())
        val key = getUserKey(requireContext())
        if (key != null && uid != null) {
            WebSocketViewModel.connect(uid, key)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Отключаемся от WebSocket, как только фрагмент становится невидимым
        WebSocketViewModel.disconnect()
    }

}
