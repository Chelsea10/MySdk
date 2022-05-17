package com.example.webviewdialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class DraglayoutActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draglayout)
        val mDrawerLayout = findViewById<DrawerLayout>(R.id.drawerLy)
        val draglayout = findViewById<LinearLayout>(R.id.right_drawer)
        mDrawerLayout.openDrawer(Gravity.RIGHT)
    }
}