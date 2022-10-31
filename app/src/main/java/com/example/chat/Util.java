package com.example.chat;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {
    //Check email valid
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //Default Img_Profile
   public static final String img_URL= "https://firebasestorage.googleapis.com/v0/b/hallo-ae1f9.appspot.com/o/img_Profile%2Fprofile.jpg?alt=media&token=39c98677-07b9-4461-9c57-c8148e4b100e";

    //Hide keyboard when finish send message
    public void hideKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
