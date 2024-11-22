package com.ilya.MeetingMap.Map.Interfaces

interface WebSocketCallback {
    fun onMessageReceived(dataList: List<WebSocketManager.ReceivedData>)
}