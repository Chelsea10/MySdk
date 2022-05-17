package com.mindlinker.webviewdialog.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import android.text.style.ClickableSpan

import android.text.style.URLSpan
import android.util.Log

import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.*

import java.net.URL

import android.text.Html.ImageGetter
import com.mindlinker.webviewdialog.R


class WebviewDialog2(
    context: Context,
    title: String,
    subText: String,
    onConfirmCallback: () -> Unit,
    onCancelCallback: () -> Unit,
    confirmButtonText: String = "",
    cancelButtonText: String = context.getString(R.string.cancel)
) : BaseDialog(context, isNeedKeyBoardListener = true) {

    private var mView: View
    var imgGetter = ImageGetter { source ->
        Log.i("RG", "source---?>>>$source")
        var drawable: Drawable? = null
        val url: URL
        try {
            url = URL(source)
            Log.i("RG", "url---?>>>$url")
            drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
        } catch (e: Exception) {
            e.printStackTrace()
            return@ImageGetter null
        }
        drawable.setBounds(
            0, 0, drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )
        Log.i("RG", "url---?>>>$url")
        drawable
    }

    init {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_weibview2, null)
        if (title.isEmpty()) {
            mView.findViewById<TextView>(R.id.title).visibility = View.GONE
        } else {
            mView.findViewById<TextView>(R.id.title).text = title
        }
        val textview = mView.findViewById<TextView>(R.id.subText)
        textview.highlightColor = Color.TRANSPARENT
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            textview.text = Html.fromHtml(subText, Html.FROM_HTML_MODE_LEGACY)
//        } else {
//            textview.text = Html.fromHtml(subText, imgGetter, tagHandler)
//        }
        textview.text = getClickableHtml(subText)
        textview.movementMethod = ScrollingMovementMethod.getInstance()// 设置可滚动
        textview.movementMethod = LinkMovementMethod.getInstance()//设置超链接可以打开网页

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
            onCancelCallback()
            dismiss()
        }
        confirmButton.setOnClickListener {
            onConfirmCallback()
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

    /**
     * html  带有<font>标签的文本
     */
    private fun getClickableHtml(html: String?): CharSequence {
        val spannedHtml: Spanned = Html.fromHtml(html)
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = clickableHtmlBuilder.getSpans(0, spannedHtml.length, URLSpan::class.java)
        for (value in spans) {
            setLinkClickable(clickableHtmlBuilder, value);
        }
        return clickableHtmlBuilder
    }



    /**
     * 捕获<a>标签点击事件
     */
    private fun setLinkClickable(clickableHtmlBuilder: SpannableStringBuilder, urlSpan: URLSpan?) {
        val start = clickableHtmlBuilder.getSpanStart(urlSpan)
        val end = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val flags = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val url = urlSpan?.url
                if (url?.contains("agreement.html")!!) {
                    Toast.makeText(context, "点击了用户协议", Toast.LENGTH_SHORT).show()
                } else if(url?.contains("privacy.html")!!){
                    Toast.makeText(context, "点击了隐私政策", Toast.LENGTH_SHORT).show()
                } else {
                    val number = url.replace("tel:", "")
                    call(number)
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                //设置颜色
                ds.color = Color.parseColor("#FF9800")
                //设置是否要下划线
                ds.isUnderlineText = false
                ds.clearShadowLayer()
            }

        }
        Log.e("======", clickableSpan.toString() + " " +start + " "+end + " "+ flags)
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
        clickableHtmlBuilder.removeSpan(urlSpan);
    }

    fun call(phoneNum: String) {
        val uri = Uri.parse("tel:$phoneNum")
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = uri
        context.startActivity(intent)
    }

}