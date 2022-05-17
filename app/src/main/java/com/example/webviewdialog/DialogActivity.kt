package com.example.webviewdialog

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast

class DialogActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        addKeyBoardShowListener(this)
    }

    fun show(view: View){
        com.mindlinker.webviewdialog.KeyboardUtils.showKeyboard(view)
    }

    fun hide(view: View){
        com.mindlinker.webviewdialog.KeyboardUtils.hideKeyboard(view)
    }

    private fun addKeyBoardShowListener(activity: Activity) {
        val rootView = activity.window.decorView
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            //　rootInvisibleHeight＝屏幕的高度 - 屏幕顶部到底部NavigationBar（安卓三大金刚）上边Y坐标
            val rootInvisibleHeight = rootView.rootView.height - rect.bottom
            // 键盘弹出时rootInvisibleHeight=键盘高度 + 底部NavigationBar（安卓三大金刚）高度
            // 键盘没弹出时，rootInvisibleHeight=底部NavigationBar（安卓三大金刚）高度,
            // 如果三大金刚是屏幕外的实体按键（比如三星S6，魅族，华为Mate10之类的）rootInvisibleHeight=0
            // 三大金刚一般高度150左右，基本在200以内，就用200做比较就可以了。
            if (rootInvisibleHeight < 200) {
                // 键盘收起
                Toast.makeText(activity, "键盘收起", Toast.LENGTH_SHORT).show()
            } else {
                // 键盘弹出
                Toast.makeText(activity, "键盘弹出", Toast.LENGTH_SHORT).show()
            }
        }
    }
}