package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.HWDTEMPT.model.SQLiteHelp;


public class login_foget extends Activity {

    private EditText ed_userName,  phonenumber;
    private Button btn_foget, btn_fogetCancel;
    String name="",password="";
    private ImageView goback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_gogett);
        foget();
    }
    
    
    // 初始化
    private void foget() {
        // TODO Auto-generated method stub
        ed_userName = (EditText) findViewById(R.id.ed_fogetuserName);
        phonenumber = (EditText) findViewById(R.id.ed_fogetnumber);
       goback=(ImageView) findViewById(R.id.about_imageview_goback);
        btn_foget = (Button) findViewById(R.id.btn_fogeterok);
        btn_fogetCancel = (Button) findViewById(R.id.btn_fogetCancel);
        btn_foget.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               register();
 
            }
        });
        btn_fogetCancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(login_foget.this, Welcome.class);
                setResult(RESULT_CANCELED, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish(); 
            }
        });
        
        goback.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(login_foget.this, Welcome.class);
                setResult(RESULT_CANCELED, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish();   
            }
        });

    }
    
    private boolean register() {
        boolean istrue=false;
        // TODO Auto-generated method stub
        name=ed_userName.getText().toString().trim();
        String phone = phonenumber.getText().toString().trim();
       
        if (name == null || name.length() < 1) {
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
        }  else if (phone == "" || phone.length() != 11) {
            Toast.makeText(this, "请输入11位手机号！", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteHelp uService = new SQLiteHelp(this);
            String sql = "select password from user where username=? and phone=?";
            Cursor cursor = uService.select(sql, new String[] {name ,phone});
            if (cursor.getCount() > 0)
            {
                istrue=true;
                cursor.moveToFirst();
                password=cursor.getString(cursor.getColumnIndex("password"));
               
                new AlertDialog.Builder(login_foget.this).setTitle("找回密码").setItems(new String[] {name,password}, null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent=new Intent();
                        intent.setClass(login_foget.this, Welcome.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("password", password);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        finish();   
                    }
                }).show();
            }else
            {
                Toast.makeText(this, "未找到此用户名注册的用户！", Toast.LENGTH_SHORT).show(); 
                istrue=false;
            }

            cursor.close();

        }
        
        return istrue;
    }
    
    
}
