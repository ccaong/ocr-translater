package com.example.ocrandtranslate.util;

import android.content.Context;
import android.content.Intent;

public class StartActivityUtil {

    /**
     * 启动新的activity
     *
     * @param context
     * @param cls
     */
    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }


}
