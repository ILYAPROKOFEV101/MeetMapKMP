package com.ilya.meetmapkmp.SocialMap.DATAServices.Find_Frinds_by_websocket

import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends


interface WebSocketCallback_frinds {
    fun onFriendListReceived(friends: List<FindFriends>)
}