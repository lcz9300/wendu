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
            // ����һ��url����
            URL url = new URL(path);
            // ͨ�^url���󣬴���һ��HttpURLConnection�������ӣ�
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setConnectTimeout(5000);
           
            // ͨ��HttpURLConnection���󣬵õ�InputStream

            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            // ʹ��io����ȡ�ļ�
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
