package com.HWDTEMPT.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.HWDTEMPT.hwdtempt.MainActivity;

public class DialogUtil {
    public static void showDialog(final Context ctx,String msg,boolean goHome) 
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(ctx)
        .setMessage(msg).setCancelable(false);
        if(goHome)
        {
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(ctx,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(i);
                }
            });
        }
        else {
            builder.setPositiveButton("ȷ��", null);
        }
        builder.create().show();
    }
    public static void showDialog(Context ctx , View view)
    {
        new AlertDialog.Builder(ctx)
        .setView(view).setCancelable(false)
        .setPositiveButton("ȷ��", null)
        .create()
        .show();
    }
  


}
