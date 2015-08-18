package com.onepiece.kuga.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.onepiece.kuga.oops.R;

public class CommonDialog {

    public static void showTips(Context context, String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(R.string.back, cancelListener)
                .create().show();
    }

    public static void showTips(Context context, int title, int msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(R.string.back, cancelListener)
                .create().show();
    }

    public static void showTips(Context context, int title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(R.string.back, cancelListener)
                .create().show();
    }

    public static void showTips(Context context, String title, int msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(R.string.back, cancelListener)
                .create().show();
    }

    public static DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    };
}


