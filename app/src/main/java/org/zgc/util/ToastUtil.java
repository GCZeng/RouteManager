package org.zgc.util;

import android.widget.Toast;

import org.zgc.app.APP;

/**
 * Created by Nick on 2017/10/2
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 短时间显示  Toast
     *
     * @param sequence
     */
    public static void showShort(CharSequence sequence) {

        if (toast == null) {
            toast = Toast.makeText(APP.sContext, sequence, Toast.LENGTH_SHORT);

        } else {
            toast.setText(sequence);
        }
        toast.show();

    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        if (null == toast) {
            toast = Toast.makeText(APP.sContext, message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(APP.sContext, message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong( int message) {
        if (null == toast) {
            toast = Toast.makeText(APP.sContext, message, Toast.LENGTH_LONG);
            //    toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示时间
     *
     * @param sequence
     * @param duration
     */
    public static void show(CharSequence sequence, int duration) {
        if (toast == null) {
            toast = Toast.makeText(APP.sContext, sequence, duration);
        } else {
            toast.setText(sequence);
        }
        toast.show();

    }

    /**
     * 隐藏toast
     */
    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
