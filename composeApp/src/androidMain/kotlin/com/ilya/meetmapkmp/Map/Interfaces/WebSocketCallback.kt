package com.ilya.meetmapkmp.Map.Interfaces

interface WebSocketCallback {
    fun onMessageReceived(dataList: List<WebSocketManager.ReceivedData>)
}