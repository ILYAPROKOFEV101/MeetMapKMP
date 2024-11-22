package com.ilya.MeetingMap.SocialMap.DataModel

import kotlinx.serialization.Serializable

@Serializable
data class FindFriends(
    val key: String,
    val name: String,
    val img: String,
    val friend: Boolean
)