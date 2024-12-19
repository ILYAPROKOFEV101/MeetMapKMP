package com.ilya.Supabase

import android.util.Log
import kotlinx.coroutines.Dispatchers

val bucketManager = BucketManager(ioDispatcher = Dispatchers.IO)

fun androidLog(tag: String): (String) -> Unit = { message ->
    Log.d(tag, message)
}