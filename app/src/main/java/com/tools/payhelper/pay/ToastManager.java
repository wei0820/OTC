package com.tools.payhelper.pay;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastManager {
    private static Toast toast;

    public static void showToastCenter(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT); //如果有居中显示需求
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setText(msg);
        toast.show();
    }
}
