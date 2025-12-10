package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class receipt : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var btnDownload: Button
    private lateinit var webview: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receipt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Rad = kotlin.random.Random.nextInt(1, 61)
        val radomUUID = java.util.UUID.randomUUID().toString().replace("-", "").uppercase().take(14)
        val totalPrice = intent.getStringExtra("totalPrice")
        Log.i("DEBUG", radomUUID)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        btnDownload = findViewById(R.id.btnDownload)
        btnBack.setOnClickListener {
            finish()
        }
        btnDownload.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("openFragment", "my_booking")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        webview = findViewById(R.id.webview)
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = false
        webview.loadUrl("https://vietqr.co/api/generate/vba/4815205123757/NGUYEN%20HUU%20TINH/${totalPrice}/THANH%20TOAN%20${radomUUID}?isMask=0&logo=1&style=0&bg=${Rad}")
    }


}