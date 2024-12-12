package com.ilya.Supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage


val supabase = createSupabaseClient(
    supabaseUrl = "https://imlhstamcqwacpgldxsf.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImltbGhzdGFtY3F3YWNwZ2xkeHNmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzIyMjA3ODAsImV4cCI6MjA0Nzc5Njc4MH0.C7HB2Q0B4WATBmilHG3oU4ZTd4TcgoYIlMfiLa4Nd6I"
) {

    //...

    install(Storage) {
        // settings
    }

}