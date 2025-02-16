package com.ilya.meetmapkmp.SocialMap.DataModel

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val token: String,
    val img: String,
    val lastmessage: String,
    val name: String,
    val online: Boolean,
    val key: String
)