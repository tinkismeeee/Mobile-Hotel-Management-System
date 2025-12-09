package com.example.androidproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupChatBot() {
        val apiKey = "AIzaSyDMGUhwRedHFpArLkz1Ti933rdCSXIxFsU"
        HotelBotManager(this, apiKey).setup()
    }
}