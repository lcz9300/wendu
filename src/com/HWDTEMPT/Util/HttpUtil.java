
package com.HWDTEMPT.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class HttpUtil {
    // 创建HttpClient对象

    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL =
            "http://210.51.26.138:89/JavaPrj_hwd/";

    /**
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String getRequest(final String url)
            throws Exception
    {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>()
                {
                    @Override
                    public String call() throws Exception
                    {
                        // 创建HttpGet对象。
                        HttpGet get = new HttpGet(url);
                        // 设置get连接和读取超时间为10秒2016.6.13下午17:06分加
                        get.getParams()
                                .setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
                        get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
                        // 发送GET请求
                        HttpResponse httpResponse = httpClient.execute(get);
                        // 如果服务器成功地返回响应
                        if (httpResponse.getStatusLine()
                                .getStatusCode() == 200)
                        {
                            // 获取服务器响应字符串
                            String result = EntityUtils
                                    .toString(httpResponse.getEntity());
                            return result;
                        }
                        return null;
                    }
                });
        new Thread(task).start();
        return task.get();
    }

    /**
     * @param url 发送请求的URL
     * @param params 请求参数
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String postRequest(final String url
            , final Map<String, String> rawParams) throws Exception
    {
        /*
         * httpClient.getParams().setParameter(CoreConnectionPNames.
         * CONNECTION_TIMEOUT,10000);
         * httpClient.getParams().setParameter(CoreConnectionPNames
         * .SO_TIMEOUT,10000);
         */
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>()
                {
                    @Override
                    public String call() throws Exception
                    {
                        // 创建HttpPost对象。
                        HttpPost post = new HttpPost(url);
                        // 设置连接/读取超时时间为10秒:2016.6.13下午17:06分加
                        post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                                10000);
                        post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
                        // 如果传递参数个数比较多的话可以对传递的参数进行封装
                        List<NameValuePair> params =
                                new ArrayList<NameValuePair>();
                        for (String key : rawParams.keySet())
                        {
                            // 封装请求参数
                            params.add(new BasicNameValuePair(key
                                    , rawParams.get(key)));
                        }
                        // 设置请求参数
                        post.setEntity(new UrlEncodedFormEntity(
                                params, "gbk"));
                        // 发送POST请求
                        HttpResponse httpResponse = httpClient.execute(post);
                        // 如果服务器成功地返回响应
                        if (httpResponse.getStatusLine()
                                .getStatusCode() == 200)
                        {
                            // 获取服务器响应字符串
                            String result = EntityUtils
                                    .toString(httpResponse.getEntity());
                            Log.e("CODE", result);
                            
                            return result;
                        }
                        return null;
                    }
                });
        new Thread(task).start();
        return task.get();
    }

}
