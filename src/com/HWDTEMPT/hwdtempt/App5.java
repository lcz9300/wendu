package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.Util.HttpUtil;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.update.UpdateInfo;
import com.HWDTEMPT.update.UpdateInfoService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App5 extends Activity {

    private RelativeLayout user,userguid,exit,mianzeLayout,updateLayout;
    private Button exitt,modifyButton;
    private View about = null, member = null, gener = null,moreView = null,userguidView = null,mianzeview = null;
    private ImageView gohomeView,pwdgohome,userguid_ImageView,mianzeimageView;
    private ProgressDialog pd;
       long exitTime=0;
       private EditText ed1,ed2;
       private TextView txtView2;
       public String  name;
       private SharedPreferences sharePren; 
       
       private UpdateInfo info;
       private ProgressDialog pBar;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       LayoutInflater inflater = this.getLayoutInflater();
       this.about = inflater.inflate(R.layout.activity_about, null);
       this.member = inflater.inflate(R.layout.pwdmodify, null);
       this.userguidView = inflater.inflate(R.layout.userguid, null);
       this.moreView = inflater.inflate(R.layout.more, null);
       this.mianzeview = inflater.inflate(R.layout.mianzesm, null);
     
       
       setContentView(moreView);
       
       sharePren = getSharedPreferences("users", MODE_PRIVATE);
       
       
   
       this.exitt=(Button) findViewById(R.id.layout_exit);
       this.user= (RelativeLayout) findViewById(R.id.layout_account);
       this.userguid = (RelativeLayout)findViewById(R.id.layout_userguid);
       this.mianzeLayout = (RelativeLayout)findViewById(R.id.layout_mianze);
       this.updateLayout =(RelativeLayout)findViewById(R.id.layout_updateapk);
     
       
       /*normal= (RelativeLayout) findViewById(R.id.layout_general);*/
       this.exit= (RelativeLayout) findViewById(R.id.layout_about);
       this.user.setOnClickListener(new ClickEvent()); 
       this.userguid.setOnClickListener(new ClickEvent());
       /*normal.setOnClickListener(new ClickEvent());  */
       this.exit.setOnClickListener(new ClickEvent());  
       this.exitt.setOnClickListener(new ClickEvent());
       this.mianzeLayout.setOnClickListener(new ClickEvent());
       this.updateLayout.setOnClickListener(new ClickEvent());
       
       updatehwd();
       
   }
   //---------------版本更新-------------
   public void updatehwd() {
   Toast.makeText(App5.this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
       
       new Thread() 
       {
           public void run() 
           {
               try {
                   UpdateInfoService updateInfoService = new UpdateInfoService(
                           App5.this);
                   info = updateInfoService.getUpDateInfo();
                   handler1.sendEmptyMessage(0);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           };
       }.start();
   }
   
   private Handler handler1 = new Handler() {
       public void handleMessage(Message msg) {
           // 如果有更新就提示
           if (isNeedUpdate()) {
               showUpdateDialog();
           }
       };
   };
 //---------------版本更新-------------
   class ClickEvent implements View.OnClickListener {                             
           @Override  
           public void onClick(View v) { 
               if(v == user){  
                   setContentView(member);
                   initpwd();
                   pwdback();
               } 
               
               if (v == userguid) {
                   
                   setContentView(userguidView);
                   userguid_back();    
               }
               if(v == exit){  
                   setContentView(about);
                   back();
               }
               if(v == mianzeLayout){
                   setContentView(mianzeview);
                   mianzeback();
               }
               if(v == updateLayout){
                   updatehwd();
               }
               if(v==exitt){
                    finish();
                       System.exit(0);//退出代码  
               }
           }         
       }
    public void pwdback()
    {
        pwdgohome = (ImageView)findViewById(R.id.pwd_imageview_gohome);
        pwdgohome.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               setContentView(moreView);
               
           }
       });
    }
    public void mianzeback()
    {
        mianzeimageView = (ImageView)findViewById(R.id.tiaokuan_imageview_gohome);
        mianzeimageView.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               setContentView(moreView);
           }
       });
    }
    public void back(){
        gohomeView = (ImageView) findViewById(R.id.about_imageview_gohome);
        gohomeView.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               setContentView(moreView);
               
           }
       });
    }
    public void userguid_back(){
        userguid_ImageView = (ImageView)findViewById(R.id.userguid_imageview_gohome);
        userguid_ImageView.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               setContentView(moreView);
           }
       });
    }
    public void initpwd()
      {
          ed1 = (EditText)findViewById(R.id.et_modify1);
          ed2 = (EditText)findViewById(R.id.et_modify2);
          txtView2 = (TextView)findViewById(R.id.pwd_textView2);
          txtView2.setText(sharePren.getString("oa_name", ""));
          modifyButton = (Button)findViewById(R.id.modify_PWD);
          modifyButton.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               switch (v.getId()) {
               case R.id.modify_PWD:
                   updatepwd();
                   break;

               default:
                   break;
               }
               
           }
       });
      }
    
   public void updatepwd()
   {
       name = sharePren.getString("oa_name", "");
       String pwd1 = ed1.getText().toString().trim();
       String pwd2 = ed2.getText().toString().trim();
       String passwordval = "^[a-zA-Z0-9]{5,9}$";
       boolean rs4 =false;
       rs4=matcher(passwordval, pwd1);
       if (pwd1 == null || pwd1.length() < 1)
       {
           Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
       } else if (!rs4) {
           DialogUtil.showDialog(this, "密码为6-10位英文+数字组合！", false);
       }
       else if (pwd2 == null || pwd2.length() < 1)
       {
           Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
       } else if (!pwd2.equals(pwd1))
       {
           DialogUtil.showDialog(this, "两次输入密码不一致，请重新输入", false);
           /*
            * Toast.makeText(this,
            * "两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            */
       }
       else
       {
           SQLiteHelp mSqLiteHelp = new SQLiteHelp(this);
           mSqLiteHelp.pwdxiugai(name, pwd2);
           
           if (netpwdmodify() != false) {
               DialogUtil.showDialog(this, "密码修改成功", false);
           }else {
               DialogUtil.showDialog(this, "密码修改失败，重新再试！", false);
           }
          
           /* Toast.makeText(this, "密码修改成功",Toast.LENGTH_SHORT).show(); */

       }
          
   }
   
   private  boolean netpwdmodify() {
       
       String username = sharePren.getString("oa_name", "");
       String password = ed2.getText().toString().trim();
       
       JSONObject jsonObj,jsObject;
       
       try {
           jsObject = netpwdJsonObject(username, password);
           if (jsObject.getString("username")!=null); 
           {
               return true;
           }
       } catch (Exception e) {
           // TODO: handle exception
           /*DialogUtil.showDialog(this, "服务器响应失败，请重新再试！", false);*/
           e.printStackTrace();
       }
       
       return false;
       
   }
   
   private JSONObject netpwdJsonObject(String username,String password) 
           throws Exception
   {
       Map<String, String> map1 = new HashMap<String, String>();
       map1.put("username",username);
       map1.put("password",password);
      
       String url1 = HttpUtil.BASE_URL + "modifyuser.do?action=updateuser_android";
       JSONObject regjsonback = new JSONObject(HttpUtil.postRequest(url1, map1));
       return regjsonback; 
   }
   private static boolean matcher(String reg, String string) {
       boolean tem = false;
       Pattern pattern = Pattern.compile(reg);
       Matcher matcher = pattern.matcher(string);
       tem = matcher.matches();
       return tem;
   }
   //------------------------以下为版本更新------------------
   private void showUpdateDialog() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setIcon(android.R.drawable.ic_dialog_info);
       builder.setTitle("请升级APP至版本" + info.getVersion());
       builder.setMessage(info.getDescription());
       builder.setCancelable(false);

       builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (Environment.getExternalStorageState().equals(
                       Environment.MEDIA_MOUNTED)) {
                   downFile(info.getUrl());
               } else {
                   Toast.makeText(App5.this, "SD卡不可用，请插入SD卡",
                           Toast.LENGTH_SHORT).show();
               }
           }
       });
       builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which) {
           }

       });
       builder.create().show();
   }

   private boolean isNeedUpdate() {
       
       String v = info.getVersion(); 
       String localv = this.getResources().getString(R.string.app_version);// 最新版本的版本号
       Log.i("版本号:",v);
       Toast.makeText(App5.this, "版本号:"+v, Toast.LENGTH_SHORT).show();
       if (v.equals(localv)) {
           return false;
         
       } else {
           return true;
       }
   }

   // 获取当前版本的版本号
   /*private String getVersion() {
       try {
           PackageManager packageManager = getPackageManager();
           PackageInfo packageInfo = packageManager.getPackageInfo(
                   getPackageName(), 0);
           return packageInfo.versionName;
       } catch (NameNotFoundException e) {
           e.printStackTrace();
           return "版本号未知";
       }
   }*/

   void downFile(final String url) { 
       pBar = new ProgressDialog(App5.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
       pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       pBar.setTitle("正在下载");
       pBar.setMessage("请稍候...");
       pBar.setProgress(0);
       pBar.show();
       new Thread() {
           public void run() {        
               HttpClient client = new DefaultHttpClient();
               HttpGet get = new HttpGet(url);
               HttpResponse response;
               try {
                   response = client.execute(get);
                   HttpEntity entity = response.getEntity();
                   int length = (int) entity.getContentLength();   //获取文件大小
                                       pBar.setMax(length);                            //设置进度条的总长度
                   InputStream is = entity.getContent();
                   FileOutputStream fileOutputStream = null;
                   if (is != null) {
                       File file = new File(
                               Environment.getExternalStorageDirectory(),
                               "xingou.apk");
                       fileOutputStream = new FileOutputStream(file);
                       //这个是缓冲区，比特值越大速度越快，这里我设置的是500.可根据实际情况更改。
                       //看不出progressbar的效果。
                       byte[] buf = new byte[500];   
                       int ch = -1;
                       int process = 0;
                       while ((ch = is.read(buf)) != -1) {       
                           fileOutputStream.write(buf, 0, ch);
                           process += ch;
                           pBar.setProgress(process);       //这里就是关键的实时更新进度了！
                       }

                   }
                   fileOutputStream.flush();
                   if (fileOutputStream != null) {
                       fileOutputStream.close();
                   }
                   down();
               } catch (ClientProtocolException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

       }.start();
   }

   void down() {
       handler1.post(new Runnable() {
           public void run() {
               pBar.cancel();
               update();
           }
       });
   }

   void update() {
       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setDataAndType(Uri.fromFile(new File(Environment
               .getExternalStorageDirectory(), "xingou.apk")),
               "application/vnd.android.package-archive");
       startActivity(intent);
   }
   
       public boolean onKeyDown(int keyCode, KeyEvent event) {  
       
              if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
              {  
                  if((System.currentTimeMillis()-exitTime) > 2000){  
                      Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();  
                      exitTime = System.currentTimeMillis();  
                  } else {  
                      finish();
                       System.exit(0);//退出代码  
                  }  
                  return true;  
              }  
              return super.onKeyDown(keyCode, event);  
           }
}
