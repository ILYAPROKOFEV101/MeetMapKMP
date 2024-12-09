package com.ilya.Supabase

import io.ktor.client.HttpClient

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging


actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000 // Таймаут запроса
            connectTimeoutMillis = 10_000 // Таймаут соединения
            socketTimeoutMillis = 10_000 // Таймаут сокета
        }
    }
}