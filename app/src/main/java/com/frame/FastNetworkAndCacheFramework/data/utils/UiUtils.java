package com.frame.FastNetworkAndCacheFramework.data.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.frame.FastNetworkAndCacheFramework.data.common.ShowDialogError;
import com.frame.FastNetworkAndCacheFramework.data.common.TokenFailedError;

import java.net.SocketTimeoutException;


public class UiUtils {

  public static void dealError(final Activity activity, Throwable throwable) {
    if (throwable instanceof TokenFailedError) {
      showToast(activity, ((TokenFailedError) throwable).message);
      // login code
    } else if (throwable instanceof ShowDialogError) {
      AlertDialog.Builder build = new AlertDialog.Builder(activity);
      build.setMessage(((ShowDialogError) throwable).message).setPositiveButton("confirm",
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
          }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      }).show();
    } else if (((!TextUtils.isEmpty(throwable.getMessage()) &&
        throwable.getMessage().startsWith("Unable to resolve host"))) ||
        (throwable.getCause() instanceof SocketTimeoutException)) {
      showToast(activity, "network error");
    } else if (throwable instanceof RuntimeException) {
      showToast(activity, throwable.getMessage());
    } else {
      toastError(activity, throwable, "network error");
    }

  }

  public static void toastError(Context context, Throwable throwable, String errorType) {
    showToast(context, errorType + ": " + throwable.getMessage());
  }

  public static void showToast(Context context, String text) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    return;
  }
}
