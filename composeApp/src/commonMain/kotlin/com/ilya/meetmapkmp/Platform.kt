package com.ilya.meetmapkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform