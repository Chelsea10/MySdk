package com.mindlinker.webviewdialog.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;

import com.mindlinker.webviewdialog.R;

public class URLDrawable extends BitmapDrawable {
      protected Drawable drawable;

     public URLDrawable(Context context) {
          this.setBounds(getDefaultImageBounds(context));

         drawable = context.getResources().getDrawable(R.drawable.ic_launcher_background);
         drawable.setBounds(getDefaultImageBounds(context));
     }

     @Override
     public void draw(Canvas canvas) {
        Log.d("test", "this=" + this.getBounds());
        if (drawable != null) {
            Log.d("test", "draw=" + drawable.getBounds());
            drawable.draw(canvas);
       }
   }

  private Rect getDefaultImageBounds(Context context) {
    Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
    int width = display.getWidth();int height = (int) (width * 9 / 16);
    Rect bounds = new Rect(0, 0, width, height);
    return bounds;
  }

}
