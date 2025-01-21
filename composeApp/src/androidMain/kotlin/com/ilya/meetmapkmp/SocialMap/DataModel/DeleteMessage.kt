package com.ilya.meetmapkmp.SocialMap.DataModel

import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessage(
    val content: String? = null, // Возможно, поле необязательно
    val delete_mesage: List<String>? = null
)