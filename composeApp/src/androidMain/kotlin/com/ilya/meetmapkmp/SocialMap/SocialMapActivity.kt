package com.ilya.meetmapkmp.SocialMap



import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ilya.MeetingMap.SocialMap.DataModel.Friend

import com.ilya.MeetingMap.SocialMap.ViewModel.FriendsViewModel
import com.ilya.MeetingMap.SocialMap.DATAServices.WebSocketListenerCallback


import com.ilya.MeetingMap.SocialMap.DATAServices.WebSocketService

import com.ilya.MeetingMap.SocialMap.ui.UI_Layers.Chat_fragment
import com.ilya.MeetingMap.SocialMap.ui.UI_Layers.FindFriends
import com.ilya.MeetingMap.SocialMap.ui.UI_Layers.FriendsScreen
import com.ilya.MeetingMap.SocialMap.ui.UI_Layers.Loop
import com.ilya.MeetingMap.SocialMap.ui.UI_Layers.MyFragmentContainer


import com.ilya.MeetingMap.SocialMap.ui.theme.SocialMap

import com.ilya.codewithfriends.presentation.profile.ID
import com.ilya.codewithfriends.presentation.sign_in.GoogleAuthUiClient
import com.ilya.reaction.logik.PreferenceHelper.getUserKey


class SocialMapActivity : FragmentActivity(), WebSocketListenerCallback{

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private lateinit var webSocketService: WebSocketService
    private val friendsViewModel: FriendsViewModel by viewModels() // Используем ViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Инициализация WebSocketService
        webSocketService = WebSocketService(this, this)


        val uid = ID(userData = googleAuthUiClient.getSignedInUser())
        val key = getUserKey(this)

        webSocketService.connect(uid.toString(), key.toString())




        enableEdgeToEdge()
        setContent {
            SocialMap {
                val navController = rememberNavController()

                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                     )
                 {
                    NavHost(navController = navController, startDestination = "Chatmenu") {
                        composable("Friendsearch") {
                            MyFragmentContainer()
                        }
                        composable("Chatmenu") {
                            Column(Modifier.fillMaxSize())
                            {
                            Loop(navController)
                            FriendsScreen(friendsViewModel.friendsList, navController, this@SocialMapActivity)
                            }
                        }
                        composable("Chat"){
                            Chat_fragment()
                        }
                    }
                }
            }
        }
    }



    override fun onStop() {
        super.onStop()
        val intent = Intent(this, WebSocketService::class.java)
        stopService(intent) // Останавливаем WebSocketService
    }

        // Реализация методов интерфейса для обработки сообщений и ошибок
    override fun onMessageReceived(message: String) {
            val listType = object : TypeToken<List<Friend>>() {}.type
            val newFriendsList: List<Friend> = Gson().fromJson(message, listType)


            // Обновляем данные в ViewModel
            friendsViewModel.updateFriends(newFriendsList)
        }


    override fun onErrorOccurred(error: String) {
        // Обработка ошибок
        println("Error occurred: $error")
        // Здесь вы можете уведомить пользователя об ошибке
     }


    override fun onDestroy() {
        super.onDestroy()
        webSocketService.closeWebSocket()
    }



}