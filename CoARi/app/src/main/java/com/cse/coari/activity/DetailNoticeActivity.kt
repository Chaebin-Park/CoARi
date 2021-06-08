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
import com.cse.coari.data.NoticeData
import com.cse.coari.helper.WebViewClientClass
import kotlinx.android.synthetic.main.activity_detail_news.*
import kotlinx.android.synthetic.main.activity_detail_notice.*

class DetailNoticeActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notice)

        detail_notice_webview.loadUrl(intent.getStringExtra("URL")!!)

        detail_notice_webview.apply {
            webViewClient = WebViewClientClass(context, detail_notice_webview, detail_notice_progressbar )

            webChromeClient = object : WebChromeClient() {
                @SuppressLint("SetJavaScriptEnabled")
                override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                    val newWebView = WebView(this@DetailNoticeActivity).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }

                    val dialog = Dialog(this@DetailNoticeActivity).apply {
                        setContentView(newWebView)
                        window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                        window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
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
            settings.mediaPlaybackRequiresUserGesture = false
            settings.allowContentAccess = true
            settings.setGeolocationEnabled(true)
            settings.allowUniversalAccessFromFileURLs = true
            settings.allowFileAccess = true
            fitsSystemWindows = true
        }
    }
}