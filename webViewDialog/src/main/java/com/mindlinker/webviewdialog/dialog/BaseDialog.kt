package com.mindlinker.webviewdialog.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.util.Log
import android.view.* // ktlint-disable no-wildcard-imports
import android.widget.Toast
import com.mindlinker.webviewdialog.R
import com.mindlinker.webviewdialog.Util
import java.lang.Exception

open class BaseDialog(val context: Context, val layBelow: Boolean = false, val isNeedKeyBoardListener: Boolean = false) {

    var mDialog: Dialog = Dialog(context, R.style.DialogStyle)

    private var dismissCallback: (() -> Unit)? = null
    private var mShowTime: Long = 0
    var cancelable: Boolean = true
    private var isKeyboardShow = false
    private var mListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var mKeyboardHeight = 0

    init {
        if (layBelow) {
            mDialog.window?.setType(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW)
        } else {
            mDialog.window?.setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW)
        }
    }

    fun showDialog() {
        showDialog(getView())
    }

    open fun getView(): View {
        return View(context)
    }

    protected fun showDialog(view: View) {
        mDialog.setContentView(view)
        val window = mDialog.window
        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            // 隐藏软键盘
            layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            layoutParams.windowAnimations = R.style.DialogAnimation
            mDialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
                override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                    handleKeyEvent(dialog, dismissCallback, keyCode)
                    when (keyCode) {
                        KeyEvent.KEYCODE_BACK -> {
                            // 快速的两次back事件会导致dialog不出现
                            if (System.currentTimeMillis() - mShowTime > 500) {
                                if (cancelable) {
                                    dialog?.dismiss()
                                }
                                dismissCallback?.invoke()
                                return true
                            }
                        }
                    }
                    return false
                }
            })
            it.attributes = layoutParams
            it.decorView.setPadding(0, 0, 0, 0)
            mDialog.setCancelable(cancelable)

            it.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

            if (isNeedKeyBoardListener) {
                addKeyBoardShowListener()
            }

            mDialog.show()

            // Set the dialog to immersive
            it.decorView.systemUiVisibility = (context as Activity).window.decorView.systemUiVisibility

            // Clear the not focusable flag from the window
            it.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        mShowTime = System.currentTimeMillis()
    }

    fun dismissDialog() {
        try {
            if (context is Activity && mListener != null) {
                context.window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(mListener)
            }
            mDialog.dismiss()
        } catch (e: Exception) {
            Log.e("BaseDialog", "ignore dismiss dialog error: $e")
        }
    }

    fun setDismissCallback(cb: () -> Unit) {
        dismissCallback = cb
    }

    open fun handleKeyEvent(dialog: DialogInterface?, dismissCallback: (() -> Unit)?, keyCode: Int) {
    }

    fun isShowing(): Boolean {
        return mDialog.isShowing
    }

    private fun addKeyBoardShowListener() {
        if (context is Activity) {
            val rootView = context.window.decorView
            if (mListener == null) {
                mListener = ViewTreeObserver.OnGlobalLayoutListener {
                    val rect = Rect()
                    rootView.getWindowVisibleDisplayFrame(rect)
                    // 　rootInvisibleHeight＝屏幕的高度 - 屏幕顶部到底部NavigationBar（安卓三大金刚）上边Y坐标
                    val rootInvisibleHeight = rootView.rootView.height - rect.bottom
                    // 键盘弹出时rootInvisibleHeight=键盘高度 + 底部NavigationBar（安卓三大金刚）高度
                    // 键盘没弹出时，rootInvisibleHeight=底部NavigationBar（安卓三大金刚）高度,
                    // 如果三大金刚是屏幕外的实体按键（比如三星S6，魅族，华为Mate10之类的）rootInvisibleHeight=0
                    // 三大金刚一般高度150左右，基本在200以内，就用200做比较就可以了。
                    if (rootInvisibleHeight < 200) {
                        if (isKeyboardShow) {
                            hideKeyboard()
                            // 键盘收起
                            isKeyboardShow = false
                            Toast.makeText(context, "键盘收起", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (!isKeyboardShow) {
                            if (mKeyboardHeight == 0) {
                                mKeyboardHeight = Util.getScreenHeight(context) - rect.height()
                            }
                            showKeyboard(mKeyboardHeight)
                            // 键盘弹出
                            isKeyboardShow = true
                            Toast.makeText(context, "键盘弹出", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            rootView.viewTreeObserver.addOnGlobalLayoutListener(mListener)
        }
    }

    open fun showKeyboard(keyboardHeight: Int) {}

    open fun hideKeyboard() {}
}
