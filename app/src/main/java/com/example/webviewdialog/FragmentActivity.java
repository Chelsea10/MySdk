package com.example.webviewdialog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
//        getFragmentManager().beginTransaction().replace(R.id.fragment, MyFragment.getInstance()).commit();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    private boolean mIsRelease = false;

    /**
     * 释放资源
     */
    private void release(){
        if (mIsRelease){
            return;
        }
        if (isFinishing()) {
            setContentView(R.layout.layout_view_null);
        }
        mIsRelease = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        release();
    }

}
