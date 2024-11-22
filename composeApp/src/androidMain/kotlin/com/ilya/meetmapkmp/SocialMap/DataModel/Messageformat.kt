package com.ilya.MeetingMap.SocialMap.DataModel

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Messageformat(
    val content: String,
    val gifUrls: List<String>,
    val imageUrls: List<String>,
    val videoUrls: List<String>,
    val fileUrls: List<String>
)