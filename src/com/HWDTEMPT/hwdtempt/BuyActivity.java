
package com.HWDTEMPT.hwdtempt;

import android.R.menu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.HWDTEMPT.Util.BoolNetConnet;



public class BuyActivity extends Activity {
    private WebView mWebView;
    private ImageView buyback;
    
    
    private static final String URL = "http://www.heart-watchdog.com";
    private static final String URL1 = "https://weidian.com/?userid=166540074";
    public static String NAMEWEB;
    public ProgressDialog progressDialog;
    public String flagString;
    private TextView titiletxt;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webmview);

        Intent intent = getIntent();

        NAMEWEB = intent.getStringExtra("username");
        flagString = intent.getStringExtra("flag");
        init();
        int NetWorkstate = BoolNetConnet.GetNetype(this);
        
        titiletxt = (TextView) findViewById(R.id.texttitleblood_webview);
        mWebView = (WebView) findViewById(R.id.android_mwebview);
        
        // ����WebViewClient����
        if (flagString != null) {
            
            if (NetWorkstate == -1) {
                titiletxt.setText("��������ʧ��");
               mWebView.setVisibility(View.GONE);
               
            }else {
                titiletxt.setText("��Ʒ����");
                mWebView.loadUrl(URL1); 
            }
            
        } else {
            
            if (NetWorkstate == -1) {
                titiletxt.setText("��������ʧ��");
                mWebView.setVisibility(View.GONE);
               
            }else {
                titiletxt.setText("�ٷ���վ");
                mWebView.loadUrl(URL); 
            }
            
            
        }

        mWebView.setWebViewClient(new MyWebViewClient());
       

        WebSettings wSet = mWebView.getSettings();
        wSet.setJavaScriptEnabled(true);
        wSet.setAllowFileAccess(true);
        wSet.setBuiltInZoomControls(true);
        // ����֧������
        wSet.setBuiltInZoomControls(true);
        // ����֧�ּ���ͼƬ
        wSet.setBlockNetworkImage(false);
        // ����֧�ֻ���
        wSet.setAppCacheEnabled(true);
        // ���û���ģʽΪĬ��
        wSet.setCacheMode(WebSettings.LOAD_DEFAULT);

    }

    private class MyWebViewClient extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// ��ҳҳ�濪ʼ���ص�ʱ��
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(BuyActivity.this);
                progressDialog.setMessage("���ݼ����У����Ե�...");
               
                progressDialog.show();
                mWebView.setEnabled(false);// ��������ҳ��ʱ����ҳ��������
            }
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {// ��ҳ���ؽ�����ʱ��
            
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                mWebView.setEnabled(true);
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) { // ��ҳ����ʱ�����ӵ���ַ
            view.loadUrl(url);
            return false;
        }
    }

    private void init() {
        buyback = (ImageView) super.findViewById(R.id.buy_imageview_gohomech);
        buyback.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* Intent intent1 = new Intent();
                intent1.putExtra("username", NAMEWEB);
                intent1.setClass(BuyActivity.this, ChoiceHome.class);
                startActivity(intent1);*/
                finish();
            }
        });
    }

    // ���û���
    // ����Activity���onKeyDown(int keyCoder,KeyEvent event)����
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())
        {
            mWebView.goBack();

            return true;
        }
        finish();// �����˳�����
        return false;

    }

}
