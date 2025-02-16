package com.ilya.meetmapkmp.SocialMap



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ilya.meetmapkmp.SocialMap.DataModel.Friend



import com.ilya.meetmapkmp.SocialMap.DATAServices.WebSocketService

import com.ilya.meetmapkmp.SocialMap.ui.UI_Layers.Chat_fragment
import com.ilya.meetmapkmp.SocialMap.ui.UI_Layers.FindFriends
import com.ilya.meetmapkmp.SocialMap.ui.UI_Layers.FriendsScreen
import com.ilya.meetmapkmp.SocialMap.ui.UI_Layers.Loop
import com.ilya.meetmapkmp.SocialMap.ui.UI_Layers.MyFragmentContainer


import com.ilya.meetmapkmp.SocialMap.ui.theme.SocialMap

import com.ilya.codewithfriends.presentation.profile.ID
import com.ilya.codewithfriends.presentation.profile.UID
import com.ilya.codewithfriends.presentation.sign_in.GoogleAuthUiClient
import com.ilya.meetmapkmp.SocialMap.Interface.MyDataProvider
import com.ilya.meetmapkmp.SocialMap.ViewModel.WebSocket_getfriendsViewModel

import com.ilya.reaction.logik.PreferenceHelper.getUserKey


class SocialMapActivity : FragmentActivity(){

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val WebSocket_getfriendsViewModel: WebSocket_getfriendsViewModel by viewModels() // Используем ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContextProvider.initialize(this)

        enableEdgeToEdge()
        setContent {
            SocialMap {
                val navController = rememberNavController()



                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "Chatmenu"
                    ) {
                        composable("Friendsearch") {
                            MyFragmentContainer()
                        }

                        composable("Chatmenu") {
                            Column(Modifier.fillMaxSize()) {
                                Loop(navController)
                                FriendsScreen(
                                    WebSocket_getfriendsViewModel,
                                    navController,
                                    this@SocialMapActivity
                                )
                            }
                        }

                        composable("Chat") {
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

    override fun onStart() {
        super.onStart()
        val uid = ID(userData = googleAuthUiClient.getSignedInUser())
        val key = getUserKey(this@SocialMapActivity)
        WebSocket_getfriendsViewModel.connect(uid.toString(), key.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocket_getfriendsViewModel.disconnect()
    }

}