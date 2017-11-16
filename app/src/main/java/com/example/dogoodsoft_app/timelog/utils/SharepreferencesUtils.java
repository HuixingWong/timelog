package com.example.dogoodsoft_app.timelog.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huixing on 2017/11/16.
 */

public class SharepreferencesUtils {

    public static boolean getIsFirstIn(Context context,String filename,String bname){

        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        boolean aBoolean = sp.getBoolean(bname, false);

        if (aBoolean){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(bname,false);
        }

        return aBoolean;

    }
}
