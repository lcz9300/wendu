
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
    // ����HttpClient����

    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL =
            "http://210.51.26.138:89/JavaPrj_hwd/";

    /**
     * @param url ���������URL
     * @return ��������Ӧ�ַ���
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
                        // ����HttpGet����
                        HttpGet get = new HttpGet(url);
                        // ����get���ӺͶ�ȡ��ʱ��Ϊ10��2016.6.13����17:06�ּ�
                        get.getParams()
                                .setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
                        get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
                        // ����GET����
                        HttpResponse httpResponse = httpClient.execute(get);
                        // ����������ɹ��ط�����Ӧ
                        if (httpResponse.getStatusLine()
                                .getStatusCode() == 200)
                        {
                            // ��ȡ��������Ӧ�ַ���
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
     * @param url ���������URL
     * @param params �������
     * @return ��������Ӧ�ַ���
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
                        // ����HttpPost����
                        HttpPost post = new HttpPost(url);
                        // ��������/��ȡ��ʱʱ��Ϊ10��:2016.6.13����17:06�ּ�
                        post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                                10000);
                        post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
                        // ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
                        List<NameValuePair> params =
                                new ArrayList<NameValuePair>();
                        for (String key : rawParams.keySet())
                        {
                            // ��װ�������
                            params.add(new BasicNameValuePair(key
                                    , rawParams.get(key)));
                        }
                        // �����������
                        post.setEntity(new UrlEncodedFormEntity(
                                params, "gbk"));
                        // ����POST����
                        HttpResponse httpResponse = httpClient.execute(post);
                        // ����������ɹ��ط�����Ӧ
                        if (httpResponse.getStatusLine()
                                .getStatusCode() == 200)
                        {
                            // ��ȡ��������Ӧ�ַ���
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
