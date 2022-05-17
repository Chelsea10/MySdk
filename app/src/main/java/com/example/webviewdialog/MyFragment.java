package com.example.webviewdialog;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class MyFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        return view;
    }

    public static MyFragment getInstance() {
        return new MyFragment();
    }

    @Override
    public void onDestroy() {
        if (getActivity() == null){
            Log.i("===================","null");
        }else {
            Log.i("===================","not null");
        }
        super.onDestroy();
    }
}
