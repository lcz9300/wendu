
package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;










import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.Util.HttpUtil;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.UserInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends Activity implements Runnable {
    private CheckBox Rememberpassword;
    private EditText et_name, et_pwd;
    private Button btnOk;
    static boolean flag = false, isFirstUse;
    private TextView btnRegister, btnForget;
    private View loginlist = null, firstpage = null;
    private SharedPreferences sp;
    private Editor ed;
    private myAsynclogin asynclogin;
    private ProgressDialog dialog = null;
    SQLiteHelp uservice = new SQLiteHelp(Welcome.this);
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    dialog.dismiss();
                    DialogUtil.showDialog(Welcome.this, "登录超时，请检查网络", false);
                    break;

                case 1:
                    setContentView(loginlist);
                    initlogin();

                    sp = getSharedPreferences("users", MODE_PRIVATE);
                    ed = sp.edit();
                    if (sp.getBoolean("ISCHECK", true)) {
                        // 将记住密码设置为被点击状
                        Rememberpassword.setChecked(true);
                        // 然后将密码传给EditText
                        et_name.setText(sp.getString("oa_name", ""));
                        et_pwd.setText(sp.getString("oa_pass", ""));
                    }

                    // 将点击的checkBOx存入到sharedprefence中
                    Rememberpassword.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Boolean isChecked1 = Rememberpassword.isChecked();
                            ed.putBoolean("ISCHECK", isChecked1);
                            ed.commit();
                        }
                    });
                    break;

            }
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = this.getLayoutInflater();
        loginlist = inflater.inflate(R.layout.login, null);
        firstpage = inflater.inflate(R.layout.firstpage, null);
        setContentView(firstpage);

        new Thread(this).start();

    }

    // 初始化登陆界面
    private void initlogin() {

        Rememberpassword = (CheckBox) findViewById(R.id.checkBox1);// 记住密码
        et_name = (EditText) findViewById(R.id.et_UserName);
        et_pwd = (EditText) findViewById(R.id.et_UserPwd);
        btnOk = (Button) findViewById(R.id.btn_Ok); 
        btnRegister = (TextView) findViewById(R.id.btn_Register);
        btnForget = (TextView) findViewById(R.id.btn_forget);
        btnOk.setOnClickListener(new ButtonListener());
        btnRegister.setOnClickListener(new ButtonListener());
        btnForget.setOnClickListener(new ButtonListener());
    }

    public void run() {

        try {

            SharedPreferences preferences = getSharedPreferences("isFirstUse",
                    1);
            isFirstUse = preferences.getBoolean("isFirstUse", true);

          

                Thread.sleep(2000);
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            
            Editor editor = preferences.edit();
            editor.putBoolean("isFirstUse", false);
            editor.commit();
        } catch (Exception e) {

        }
    }

    class ButtonListener implements OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_Ok:
                    /*netlogin();*/
                    if (validate()) 
                    {
                        login();
                    }
                    break;
                case R.id.btn_Register:
                    Intent intent = new Intent();
                    /*intent.setClass(Welcome.this, Register.class);*/
                    intent.setClass(Welcome.this, NetRegister.class);
                    startActivityForResult(intent, 0);
                    break;

                case R.id.btn_forget:
                    Intent intent1 = new Intent();
                    intent1.setClass(Welcome.this, login_foget.class);
                    startActivityForResult(intent1, 1);
                    break;

                default:
                    break;
            }
        }
    }
    class myAsynclogin extends AsyncTask<Void, String, Void>{
        
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = ProgressDialog.show(Welcome.this, "登录提示", "正在登录，请稍等...",false);
            
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String username = et_name.getText().toString().trim();
            String password = et_pwd.getText().toString().trim();
            
            ed.putString("oa_name", username);
            ed.putString("oa_pass", password);
            ed.commit();
            
            JSONObject jsonObj;
            try {
                jsonObj = query(username, password);
                String backvalueString = jsonObj.getString("username");
                publishProgress(backvalueString);
            } catch (Exception e) {
                // TODO: handle exception
                timeoutTest();
            }
            
            return null;
        }
        
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
            dialog.dismiss();
            String username = et_name.getText().toString().trim();
            String password = et_pwd.getText().toString().trim();
            String phone = "";
            String sexstr = "";
            String age = "";
            String userHeight = "";
            String useWeight = "";
            if (values[0].equals(username)) {
                Toast.makeText(Welcome.this, "登陆成功", Toast.LENGTH_SHORT).show();
                
                UserInfo user = new UserInfo();
                user.setUserName(username);
                user.setUserPwd(password);
                user.setPhone(phone);
                user.setUserSex(sexstr);
                user.setUserAge(age);
                user.setUserHeight(userHeight);
                user.setUseWeight(useWeight);
                uservice.register(user);
                
                Intent mIntent = new Intent();
               mIntent.putExtra("username", username);
                mIntent.setClass(Welcome.this, MainActivity.class);
                 startActivity(mIntent);
                 Welcome.this.finish();
                
                
                
            }else {
                
                Toast.makeText(Welcome.this, "登陆失败", Toast.LENGTH_SHORT).show();
                DialogUtil.showDialog(Welcome.this, "登录失败，请检查用户名密码是否正确！", false);
            }
        }
       
    }
    //登录超时为5秒钟
    private void timeoutTest() {  
        // TODO Auto-generated method stub  
        new Timer().schedule(new TimerTask() {  
  
            @Override  
            public void run() {  
                Message msgMessage = new Message();  
                msgMessage.what = 2;  
                myHandler.sendMessage(msgMessage);  
            }  
        }, 10 * 1000);  
    }  
  //用户名服务器验证
   /* public void netlogin()
    {
        if (validate())
        {

            if (loginPro())
            {
                PrograssBar.showprograss(this);

                myHandler.post(new Runnable() {
                    public void run() {
                        String username = et_name.getText().toString().trim();
                        Intent mIntent = new Intent();
                        mIntent.putExtra("username", username);
                        mIntent.setClass(Welcome.this, MainActivity.class);
                        startActivity(mIntent);
                        Welcome.this.finish();
                    }
                });
               
                
            }
            else
            {
                DialogUtil.showDialog(Welcome.this
                        , "用户名称或者密码错误，请重新输入！", false);

            }
        }
    }
*/
    /*private boolean loginPro()
    {
       
        
        String username = et_name.getText().toString().trim();
        String password = et_pwd.getText().toString().trim();
        
        ed.putString("oa_name", username);
        ed.putString("oa_pass", password);
        ed.commit();
        
        
        JSONObject jsonObj;
        try {
            jsonObj = query(username, password);
            if (jsonObj.getString("username") != null);
            {
               
                Toast.makeText(Welcome.this, "登录成功,", Toast.LENGTH_LONG).show();
                
                return true;
            }

        } catch (Exception e) {
             DialogUtil.showDialog(this, "服务器响应失败，请稍后再试！", false); 
            e.printStackTrace();
        }
        return false;
    }
*/
    private boolean validate()
    {
        String username = et_name.getText().toString().trim();
        if (username.equals(""))
        {
            DialogUtil.showDialog(this, "用户名必填", false);
            return false;
        }
        String password = et_pwd.getText().toString().trim();
        if (password.equals(""))
        {
            DialogUtil.showDialog(this, "密码必填", false);
            return false;
        }
        return true;
    }

    private JSONObject query(String username, String password)
            throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        String url = HttpUtil.BASE_URL + "logon.do?action=logon";
        JSONObject jsonback = new JSONObject(HttpUtil.postRequest(url, map));
        return jsonback;

    }
  
    public void login() {
        // TODO Auto-generated method stub
        String name = et_name.getText().toString();
        String password = et_pwd.getText().toString();
     // 将信息存入到users里面
        ed.putString("oa_name", name);
        ed.putString("oa_pass", password);
        ed.commit();
        
       
        flag = uservice.login(name, password);
        
       
        if (flag) {
            //Log.i("ECG", "登陆成功");
            
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent();
                intent2.putExtra("flag", true);
                intent2.putExtra("username", name);
                intent2.setClass(Welcome.this, MainActivity.class);
                startActivity(intent2);
                Welcome.this.finish();
           
            
        } else {
            asynclogin = new myAsynclogin();
            asynclogin.execute();
            Toast.makeText(this, "服务器验证...", Toast.LENGTH_SHORT).show();
            
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); // data为B中回传的Intent
                String name = b.getString("name");// str即为回传的值
                String password = b.getString("password");// str即为回传的值
                setContentView(loginlist);
                et_name.setText(name);
                et_pwd.setText(password);
                break;
            default:
                setContentView(loginlist);
                break;
        }
    }

}