package com.mindlinker.webviewdialog.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.* // ktlint-disable no-wildcard-imports
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mindlinker.webviewdialog.R
import java.net.URL

class WebviewDialog3(
    context: Context,
    title: String,
    subText: String,
    onConfirmCallback: () -> Unit,
    onCancelCallback: () -> Unit,
    confirmButtonText: String = "",
    cancelButtonText: String = context.getString(R.string.cancel)
) : BaseDialog(context) {

    var drawable: Drawable? = null
    lateinit var textview: TextView
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            msg?.let { message ->
                when (message.what) {
                    1 -> {
                        drawable = message.obj as Drawable
                        drawable?.setBounds(
                            0, 0, drawable!!.intrinsicWidth,
                            drawable!!.intrinsicHeight
                        )
//                        imageView.setImageDrawable(drawable)
//                        textview.text = textview.text
//                        textview.invalidate()
                    }
                    else -> {}
                }
            }
        }
    }

    private var mView: View
    var imgGetter = ImageGetter { source ->
        if (drawable == null) {
            Log.i("RG", "source---?>>>$source")
            val url: URL
            try {
                url = URL(source)
                Log.i("RG", "url---?>>>$url")
                Thread(object : Runnable {
                    override fun run() {
                        val msg = Message.obtain()
                        msg.what = 1
                        msg.obj = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
                        mHandler.sendMessage(msg)
                    }
                }).start()
//            drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
            } catch (e: Exception) {
                e.printStackTrace()
                return@ImageGetter null
            }
            Log.i("RG", "url---?>>>$url")
        }
        drawable
    }

    private var tagHandler = TagHandler { opening, tag, output, xmlReader ->
        Toast.makeText(context, tag, Toast.LENGTH_SHORT).show()
    }

    init {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_weibview2, null)
        if (title.isEmpty()) {
            mView.findViewById<TextView>(R.id.title).visibility = View.GONE
        } else {
            mView.findViewById<TextView>(R.id.title).text = title
        }
        textview = mView.findViewById<TextView>(R.id.subText)
//        textview.visibility = View.INVISIBLE
        textview.highlightColor = Color.TRANSPARENT
//        textview.text = getClickableHtml(subText)
//        textview.text = Html.fromHtml(subText, URLImageGetter(context,textview), SizeLabel(100))
        textview.text = Html.fromHtml(
            subText,
            URLImageGetter(
                context,
                textview
            ),
            null
        )
//        HtmlParser.setHtml(textview, subText, null)
        textview.movementMethod = ScrollingMovementMethod.getInstance() // 设置可滚动
        textview.movementMethod = LinkMovementMethod.getInstance() // 设置超链接可以打开网页

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
//        val staticLayoutView = mView.findViewById<StaticLayoutView>(R.id.static_layout_view)
//        staticLayoutView.setLayout(StaticLayoutManager.getInstance().getLongStringLayout());
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
            setLinkClickable(clickableHtmlBuilder, value)
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
                } else {
                    Toast.makeText(context, "点击了隐私政策", Toast.LENGTH_SHORT).show()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                // 设置颜色
                ds.color = Color.parseColor("#FF9800")
                // 设置是否要下划线
                ds.isUnderlineText = false
                ds.clearShadowLayer()
            }
        }
        Log.e("======", clickableSpan.toString() + " " + start + " " + end + " " + flags)
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags)
        clickableHtmlBuilder.removeSpan(urlSpan)
    }
}
