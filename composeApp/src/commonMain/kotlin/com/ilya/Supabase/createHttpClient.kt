package com.ilya.Supabase

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient


expect fun getFileBytes(): ByteArray
