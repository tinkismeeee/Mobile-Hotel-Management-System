package com.example.androidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class about_fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webview = view.findViewById<WebView>(R.id.webview)
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = false
        webview.loadUrl("http://217.216.72.223:5000/about")

    }


}