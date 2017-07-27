package com.HWDTEMPT.Util;

import android.content.Context;

import com.HWDTEMPT.hwdtempt.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpDownDate {
    public HttpDownDate(Context context){
        
    }
    
    public static  String getDownDate(String filename,String username) throws Exception
    {
        String path = HttpUtil.BASE_URL+"cache_txt/"+username+"/"+filename;
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try { 
            // 创建一个url对象
            URL url = new URL(path);
            // 通過url对象，创建一个HttpURLConnection对象（连接）
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setConnectTimeout(5000);
           
            // 通过HttpURLConnection对象，得到InputStream

            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            // 使用io流读取文件
            while ((line = reader.readLine()) != null) {

                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
        
    }

}
