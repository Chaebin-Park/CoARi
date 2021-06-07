package com.cse.coari.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import com.cse.coari.R
import com.cse.coari.data.NewsData
import kotlinx.android.synthetic.main.activity_detail_news.*

class DetailNewsActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        news_webview.apply {
            webViewClient = WebViewClientClass()

            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                    val newWebView = WebView(this@DetailNewsActivity).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }

                    val dialog = Dialog(this@DetailNewsActivity).apply{
                        setContentView(newWebView)
                        window!!.attributes.width   = ViewGroup.LayoutParams.MATCH_PARENT
                        window!!.attributes.height  = ViewGroup.LayoutParams.MATCH_PARENT
                        show()
                    }

                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView?) {
                            dialog.dismiss()
                        }
                    }

                    (resultMsg?.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }

            settings.javaScriptEnabled = true
            settings.setSupportMultipleWindows(true)    // 새창 띄우기
            settings.javaScriptCanOpenWindowsAutomatically = true   // 자바스크립트 새창띄우ㅡ기
            settings.loadWithOverviewMode = true    // 메타태그 허용여부
            settings.useWideViewPort = true // 화면 사이즈 맞추기 허용여부
            settings.setSupportZoom(true)   // 화면 줌 여부
            settings.builtInZoomControls = true // 화면 확대 축소 허용여부
            settings.cacheMode = WebSettings.LOAD_NO_CACHE  // 브라우저 캐시 허용여부
            settings.domStorageEnabled = true   // 로컬 저장소 허용여부
            settings.displayZoomControls = true

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                settings.safeBrowsingEnabled = true // api 26
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                settings.mediaPlaybackRequiresUserGesture = false
            }
            settings.allowContentAccess = true
            settings.setGeolocationEnabled(true)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                settings.allowUniversalAccessFromFileURLs = true
            }
            settings.allowFileAccess = true
            fitsSystemWindows = true
        }

        val data = intent.getSerializableExtra("data") as NewsData
        val url = data.url
        news_webview.loadUrl(url)
    }

    // 웹 뷰에서 홈페이지를 띄웠을 때, 기존창에서 실행되도록 하기 위함.
    inner class WebViewClientClass: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if(url != null)     view?.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            news_progressbar.visibility = ProgressBar.VISIBLE
            news_webview.visibility = View.GONE
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            news_progressbar.visibility = ProgressBar.GONE
            news_webview.visibility = View.VISIBLE
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@DetailNewsActivity)
            var message = "SSL Certificate error"
            when(error?.primaryError) {
                SslError.SSL_UNTRUSTED  -> message = "The certificate authority is not trusted."
                SslError.SSL_EXPIRED    -> message = "The certificate has expired."
                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID-> message = "The certificate is not yet valid"
            }
            message += " Do you want to continue anyway?"
            builder.setTitle("SSL Certificate Error")
            builder.setMessage(message)
            builder.setPositiveButton("continue",
                DialogInterface.OnClickListener{ _, _ ->
                    handler?.proceed()
                })
            builder.setNegativeButton("cancel",
                DialogInterface.OnClickListener{ dialog, which ->
                    handler?.cancel()
                })
            val dialog: android.app.AlertDialog? = builder.create()
            dialog?.show()
        }
    }
}