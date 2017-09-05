package com.example.user.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by user on 8/29/2017.
 */

public class OverallMethods extends AppCompatActivity{

    public boolean checkCredetntials(String[] parameters) {
        int max_lenght = parameters.length - 1;
        for(int i = 0; i <= max_lenght; i++)
            if(parameters[i].length() == 0)
                return false;

        return true;
    }

    public void hideKeyboard(Activity activity) {

        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }

    }

    public Bitmap getBitmapFromStringBase64(String bitmapStr) {
        byte[] img = Base64.decode(bitmapStr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        return getRoundedBitmap(bitmap);
    }

    //returns a bitmap containing the rounded version of the original image
    public Bitmap getRoundedBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 300;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
