package com.example.androidproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupChatBot() {
        val apiKey = BuildConfig.GEMINI_API_KEY
        HotelBotManager(this, apiKey).setup()
    }
}