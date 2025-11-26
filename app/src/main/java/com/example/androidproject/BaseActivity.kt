package com.example.androidproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupChatBot() {
        val apiKey = "AIzaSyCyf7FnX06XvYJ-h2g_ISE_aOc9jv4PXrM"
        HotelBotManager(this, apiKey).setup()
    }
}