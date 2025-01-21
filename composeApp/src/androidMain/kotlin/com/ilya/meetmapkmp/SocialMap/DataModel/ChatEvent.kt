package com.ilya.meetmapkmp.SocialMap.DataModel

sealed class ChatEvent {
    data class Message(val message: Messages_Chat) : ChatEvent()
    data class Delete(val deleteMessage: DeleteMessage) : ChatEvent()
}
