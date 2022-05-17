package com.mindlinker.webviewdialog.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class URLImageGetter implements Html.ImageGetter {
      Context context;
      TextView textView;

      public URLImageGetter(Context context, TextView textView) {
          this.context = context;
          this.textView = textView;
      }

     @Override
     public Drawable getDrawable(String paramString) {
         final URLDrawable urlDrawable = new URLDrawable(context);

        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
     }

     public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
         URLDrawable urlDrawable;

         public ImageGetterAsyncTask(URLDrawable drawable) {
             this.urlDrawable = drawable;
         }

         @Override
         protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;
                URLImageGetter.this.textView.requestLayout();
                URLImageGetter.this.textView.setVisibility(View.VISIBLE);
           }
        }

        @Override
         protected Drawable doInBackground(String... params) {
            String source = params[0];
            URL url = null;
            try {
               url = new URL(source);
               Drawable drawable =  Drawable.createFromStream(url.openStream(), "");
               drawable.setBounds(
                     0, 0, drawable.getIntrinsicWidth(),
                     drawable.getIntrinsicHeight());
               return drawable;
            } catch (Exception e) {
               e.printStackTrace();
               return null;
            }
//            return fetchDrawable(source);
        }


//        public Drawable fetchDrawable(String url) {
//          try {
//               InputStream is = fetch(url);
//
//               Rect bounds = getDefaultImageBounds(context);
//               Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
//                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg, bounds.right, bounds.bottom, true);
//
//               BitmapDrawable drawable = new BitmapDrawable(bitmap);
//                drawable.setBounds(bounds);
//
//                return drawable;
//           } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        private InputStream fetch(String url) throws Exception {
//           DefaultHttpClient client = new DefaultHttpClient();
//            HttpGet request = new HttpGet(url);
//
//            HttpResponse response = client.execute(request);
//             return response.getEntity().getContent();
//        }
     }

    private Rect getDefaultImageBounds(Context context) {
         Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
          int width = display.getWidth();int height = (int) (width * 9 / 16);
         Rect bounds = new Rect(0, 0, width, height);
         return bounds;
      }
 }
