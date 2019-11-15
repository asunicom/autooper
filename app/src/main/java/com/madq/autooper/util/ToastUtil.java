package com.madq.autooper.util;

import java.util.Collection;

import android.widget.Toast;
import com.madq.autooper.App;

public class ToastUtil {

    public static void toast(CharSequence cs) {
        Toast.makeText(App.context, cs, Toast.LENGTH_SHORT).show();
    }

    //集合是否是空的
    public static boolean isEmptyArray(Collection list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmptyArray(T[] list) {
        return list == null || list.length == 0;
    }
}
