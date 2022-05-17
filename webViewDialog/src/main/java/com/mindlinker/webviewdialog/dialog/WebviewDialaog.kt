package com.mindlinker.webviewdialog.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.* // ktlint-disable no-wildcard-imports
import android.widget.TextView
import android.widget.Toast
import com.mindlinker.webviewdialog.R
import com.mindlinker.webviewdialog.Util

class WebviewDialaog(
    context: Context,
    title: String,
    url: String,
    onConfirmCallback: () -> Unit,
    onCancelCallback: () -> Unit,
    confirmButtonText: String = "",
    cancelButtonText: String = context.getString(R.string.cancel)
) : BaseDialog(context, isNeedKeyBoardListener = true) {

    private var mView: View

    init {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_weibview, null)
        if (title.isEmpty()) {
            mView.findViewById<TextView>(R.id.title).visibility = View.GONE
        } else {
            mView.findViewById<TextView>(R.id.title).text = title
        }
        val webview = mView.findViewById<WebView>(R.id.webview)

        webview.loadUrl(url)
        webview.webViewClient = MyClient()
        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
//        webSettings.supportZoom();
//        webSettings.loadWithOverviewMode;
//        webSettings.useWideViewPort;
//        webSettings.defaultTextEncodingName = "utf-8"
//        webSettings.loadsImagesAutomatically
        val myJavaScriptInterface = JavaScriptInterface(context)
        webview.addJavascriptInterface(myJavaScriptInterface, "test")
        val confirmButton = mView.findViewById<TextView>(R.id.confirmBtn)
        if (confirmButtonText.isNotEmpty()) {
            confirmButton.text = confirmButtonText
        }
        val cancelButton = mView.findViewById<TextView>(R.id.cancelBtn)
        if (cancelButtonText.isNotEmpty()) {
            cancelButton.text = cancelButtonText
            cancelButton.visibility = View.VISIBLE
        } else {
            cancelButton.visibility = View.GONE
        }
        cancelButton.setOnClickListener {
            onCancelCallback.invoke()
            dismiss()
        }
        confirmButton.setOnClickListener {
            onConfirmCallback.invoke()
            dismiss()
        }
        cancelable = false
    }

    fun show() {
        dismiss()
        try {
            super.showDialog(mView)
        } catch (e: Exception) {
        }
    }

    fun dismiss() {
        super.dismissDialog()
    }

    inner class MyClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val shouldOverrideUrlLoading = super.shouldOverrideUrlLoading(view, request)
            show()
            return shouldOverrideUrlLoading
        }
    }

    class JavaScriptInterface(var mContext: Context) {

        @JavascriptInterface
        fun call(phoneNum: String) {
//            Toast.makeText(mContext, phoneNum, Toast.LENGTH_SHORT).show()
            val uri = Uri.parse("tel:$phoneNum")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = uri
            mContext.startActivity(intent)
        }

        @JavascriptInterface
        fun toastAndroid(str: String) {
            Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showKeyboard(keyboardHeight: Int) {
        val layoutParams = mView.layoutParams
        layoutParams.height = Util.getScreenHeight(context) - keyboardHeight
        mView.layoutParams = layoutParams
        super.showKeyboard(keyboardHeight)
    }

    override fun hideKeyboard() {
        val layoutParams = mView.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        mView.layoutParams = layoutParams
        super.hideKeyboard()
    }
}
