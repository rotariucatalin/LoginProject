package com.example.user.test;

import android.app.Activity;
import android.view.MotionEvent;

/**
 * Created by user on 8/29/2017.
 */

public interface OverallMethods {

    boolean checkCredetntials(String[] parameters);

    void hideKeyboard(Activity activity);

    boolean dispatchTouchEvent(MotionEvent ev);

}
