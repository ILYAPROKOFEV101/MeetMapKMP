package com.ilya.meetmapkmp.SocialMap.Interface

import com.ilya.MeetingMap.SocialMap.DataModel.FindFriends


interface WebSocketCallback_frinds {
    fun onFriendListReceived(friends: List<FindFriends>)
}