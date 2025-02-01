package com.ilya.meetmapkmp.SocialMap.DataModel

import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageContent(
    val deleteMessages: List<String>? = null
)