package com.cse.coari.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class WebViewClientClass(var context: Context, var webView: WebView, var progressBar: ProgressBar): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if(url != null)     view?.loadUrl(url)
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressBar.visibility = ProgressBar.VISIBLE
        webView.visibility = View.GONE
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        progressBar.visibility = ProgressBar.GONE
        webView.visibility = View.VISIBLE
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
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
        builder.setPositiveButton("continue"
        ) { _, _ ->
            handler?.proceed()
        }
        builder.setNegativeButton("cancel"
        ) { _, _ ->
            handler?.cancel()
        }
        val dialog: android.app.AlertDialog? = builder.create()
        dialog?.show()
    }
}