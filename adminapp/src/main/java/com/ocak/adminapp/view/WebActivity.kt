package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val web=intent.getStringExtra("web").toString()
        webView.webViewClient= WebViewClient()
        webView.loadUrl(web)
        val webSettings : WebSettings = webView.settings
        webSettings.javaScriptEnabled=true

    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }


    }
}