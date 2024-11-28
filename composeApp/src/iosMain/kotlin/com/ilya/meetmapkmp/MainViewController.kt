package com.ilya.meetmapkmp

import com.ilya.meetmapkmp.SocialMap.DataModel.Messages_Chat
import kotlinx.coroutines.flow.StateFlow

interface WebSocketInterface {
    val messages: StateFlow<List<Messages_Chat>>
    fun connect()
    fun sendMessage(message: Messages_Chat)
    fun disconnect()
}



