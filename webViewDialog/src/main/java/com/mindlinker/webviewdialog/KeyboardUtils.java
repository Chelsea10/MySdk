package com.mindlinker.webviewdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * 软键盘处理类
 *
 * @author wujiabao
 * @date 2019-11-22 08:56.
 */
public class KeyboardUtils {

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    public static void  toggleSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0,0);
        }
    }

    /**
     * 软键盘造成的内存泄露，在调用了show或者hide之后也没destory时调用该方法
     * @param context
     */
    @SuppressLint("DiscouragedPrivateApi")
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj = null;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj = f.get(imm);
                if (obj instanceof View) {
                    View vGet = (View) obj;
                    if (vGet.getContext() == context) {
                        f.set(imm, null);
                    } else {
                        break;
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addKeyBoardShowListener(Activity activity) {
        final View rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                //　rootInvisibleHeight＝屏幕的高度 - 屏幕顶部到底部NavigationBar（安卓三大金刚）上边Y坐标
                int rootInvisibleHeight = rootView.getRootView().getHeight() - rect.bottom;
                // 键盘弹出时rootInvisibleHeight=键盘高度 + 底部NavigationBar（安卓三大金刚）高度
                // 键盘没弹出时，rootInvisibleHeight=底部NavigationBar（安卓三大金刚）高度,
                // 如果三大金刚是屏幕外的实体按键（比如三星S6，魅族，华为Mate10之类的）rootInvisibleHeight=0
                // 三大金刚一般高度150左右，基本在200以内，就用200做比较就可以了。
                if (rootInvisibleHeight < 200) {
                    // 键盘收起
                }else{
                    // 键盘弹出
                }
            }
        });
    }
}
