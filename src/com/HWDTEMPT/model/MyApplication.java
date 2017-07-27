package com.HWDTEMPT.model;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    
    
    private static Context context; 
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        context = getApplicationContext();
    }

    
  //·µ»Ø  
    public static Context getContextObject(){  
        return context;  
    } 
    
}
