package com.HWDTEMPT.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
    private Context context = null;
    public NetWorkUtil(Context context){
        this.context = context;
    }
    
    public boolean isNetWorkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }else {
                return false;
                
            }
        }else {
            return false;
        }
    }

}
