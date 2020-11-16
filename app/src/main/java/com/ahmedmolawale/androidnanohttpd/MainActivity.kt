package com.ahmedmolawale.androidnanohttpd

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ahmedmolawale.androidnanohttpd.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var localWebserver: LocalWebserver
    private val PORT = 8094 //port must not be in use otherwise you will get an exception :)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        localWebserver = LocalWebserver(this, PORT)
        localWebserver.start()
        setupWebview("http://localhost:$PORT/index.html")
    }

    private fun setupWebview(data: String) {
        binding.progressBar.max = 100
        binding.progressBar.progress = 1
        binding.webview.loadUrl(data)
        with(binding.webview) {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            setBackgroundColor(Color.TRANSPARENT)
            isVerticalScrollBarEnabled = false
        }
        with(binding.webview.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            domStorageEnabled = true
            minimumFontSize = 10
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
        }

        binding.webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.progress = newProgress
            }
        }
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.destroy()
        localWebserver.stop()
    }
}
