package com.ilya.meetmapkmp.Map.DataModel

import kotlinx.serialization.Serializable

@Serializable
data class Friends_type(
    val name: String,
    val img: String,
    val key: String,
)