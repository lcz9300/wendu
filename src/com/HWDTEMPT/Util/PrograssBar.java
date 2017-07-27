package com.HWDTEMPT.Util;

import android.app.ProgressDialog;
import android.content.Context;

public class PrograssBar {
    @SuppressWarnings("unused")
    private static ProgressDialog progressDialog = null;
    
    public static void showprograss(Context ctx){
         progressDialog = ProgressDialog.show(ctx, "请稍等...", "正在服务器验证...", true);
         
    }
/*    public static void showprograss1(Context ctx){
         progressDialog = ProgressDialog.show(ctx, "请稍等", "正在提交服务器...", true);
         
    }*/

}
