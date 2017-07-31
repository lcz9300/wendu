
package com.HWDTEMPT.hwdtempt;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.Util.HttpUtil;
import com.HWDTEMPT.Util.PrograssBar;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.UserInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class NetRegister extends Activity {
    private Button RegBtn, reg1btn, rcancereg1btn;
    private ImageView imagback;
    private EditText mEdituname, mEditupass1, mEditupass2, mphone, mtall, mweight, mage;
    private RadioGroup sexradio;
    private View userinforeg, netuserinfo;
   // private myAsynclogin1 asynclogin1 = new myAsynclogin1();
   // private myAsynclogin2 asynclogin2 = new myAsynclogin2();
    //private myAsynclogin3 asynclogin3 = new myAsynclogin3();
    private ImageView goback;
    private ProgressDialog pd;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy");
    private Calendar cal = Calendar.getInstance();
    private Date today = new Date(); 
    private Date today1 = new Date(); 
    SQLiteHelp mService = new SQLiteHelp(this);
    

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = this.getLayoutInflater();
        userinforeg = inflater.inflate(R.layout.register1, null);
        netuserinfo = inflater.inflate(R.layout.netreg1, null);
        setContentView(netuserinfo);
        init();
        back();

    }

    private void inituserinfo() {
        DialogUtil.showDialog(this, "请保持信息正确性有助于提高诊断的准确性。", false);
        mphone = (EditText) findViewById(R.id.register1_et_phone);
        mtall = (EditText) findViewById(R.id.register1_et_tall);
        mweight = (EditText) findViewById(R.id.register1_et_weght);
        mage = (EditText) findViewById(R.id.register1_et_old);
        goback = (ImageView) findViewById(R.id.register1_imageview_goback);
        sexradio = (RadioGroup) findViewById(R.id.register1_radioGroup);

        reg1btn = (Button) findViewById(R.id.register1_btnreg1);// 保存按钮
        rcancereg1btn = (Button) findViewById(R.id.register1_btncance1);// 取消按钮
        reg1btn.setOnClickListener(new btnclick());
        rcancereg1btn.setOnClickListener(new btnclick());

        goback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setContentView(netuserinfo);
            }
        });
      
    }

    private void init() {

        // TODO Auto-generated method stub
        mEdituname = (EditText) findViewById(R.id.netreg1_et_name);
        mEditupass1 = (EditText) findViewById(R.id.netreg1_et_password);
        mEditupass2 = (EditText) findViewById(R.id.netreg1_et_password1);

        RegBtn = (Button) findViewById(R.id.netreg1_btnreg);// 下一步按钮
        RegBtn.setOnClickListener(new btnclick());

       
    }
    class btnclick implements OnClickListener{
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.netreg1_btnreg:
                    if (validate()) 
                    {
                       
                        if (checkname()) 
                        {
                          
                            setContentView(userinforeg);
                            inituserinfo();
                        }
                        //asynclogin1.execute();
                    }
                    break;
                case R.id.register1_btnreg1:
                    if (validateinfo1()) 
                    {
                       
                        if (checkname())
                        {
                            if (regPro())
                            {
                                localreg();
                               
                                NetRegister.this.finish();

                            }
                        }
                        //asynclogin2.execute();
                    }
                    break;
                case R.id.register1_btncance1:
                    if (validateinfo()) {
                      
                        if (checkname())
                        {
                            if (regPro())
                            {
                                localreg();
                              
                                NetRegister.this.finish();

                            }
                        }
                        //asynclogin2.execute();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /*class myAsynclogin2 extends AsyncTask<Void, String, Void>{
        protected void onPreExecute(){
            super.onPreExecute();
            pd = ProgressDialog.show(NetRegister.this, "提示", "正在验证用户名，请稍等...",false);
            
        }

        @Override
        protected Void doInBackground(Void... params) {
            String username = mEdituname.getText().toString().trim();
            String password = mEditupass1.getText().toString().trim();
            String phone = mphone.getText().toString().trim();
            String tall = mtall.getText().toString().trim();
            String weihgt = mweight.getText().toString().trim();
             String age = mage.getText().toString().trim(); 
            String ecgsex = ((RadioButton) NetRegister.this.findViewById(sexradio
                    .getCheckedRadioButtonId())).getText().toString();
            JSONObject jsObject,jsonnameObject1;

            try {
                jsonnameObject1 = checknameurl(username);
                String backvalueString2 = jsonnameObject1.getString("sta");
                jsObject = regadd(username, password, phone, tall, ecgsex, weihgt);
                String backvalueString1 = jsObject.getString("username");
            } catch (Exception e) {
               
                e.printStackTrace();
            }

            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            pd.dismiss();
            if (values[0]!= null) {
               if (values[1]!= null) {
                   localreg();
                   
                   NetRegister.this.finish();
            }
                
                
            }
        }
        
    }
  
   class myAsynclogin1 extends AsyncTask<Void, String, Void>{
       
       protected void onPreExecute(){
           super.onPreExecute();
           pd = ProgressDialog.show(NetRegister.this, "提示", "正在验证用户名，请稍等...",false);
           
       }

    @Override
    protected Void doInBackground(Void... params) {
        String username = mEdituname.getText().toString().trim();

        JSONObject jsonnameObject;
        try {
            jsonnameObject = checknameurl(username);
            String backvalueString = jsonnameObject.getString("sta");
            publishProgress(backvalueString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        return null;
    }
    protected void onProgressUpdate(String... values){
        super.onProgressUpdate(values);
        pd.dismiss();
        if (values[0].equals("ok")) {
            setContentView(userinforeg);
            inituserinfo();
        }else {
            DialogUtil.showDialog(NetRegister.this, "用户名存在或手机未上网，打开手机上网功能，换一个名称试试吧！", false);
        }
        
        
        }
       
   }*/
    // -----------------执行网络推送，带参数-----------
    private boolean checkname() {
        String username = mEdituname.getText().toString().trim();

        JSONObject jsonnameObject;
        try {
            jsonnameObject = checknameurl(username);

            if (jsonnameObject.getString("sta") != null)
                ;
            {

                return true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            DialogUtil.showDialog(this, "用户名存在或手机未上网，打开手机上网功能，换一个名称试试吧！", false);
            e.printStackTrace();
        }
        return false;
    }

    private boolean regPro()
    {
        String username = mEdituname.getText().toString().trim();
        String password = mEditupass1.getText().toString().trim();
        String phone = mphone.getText().toString().trim();
        String tall = mtall.getText().toString().trim();
        String weihgt = mweight.getText().toString().trim();
         String age = mage.getText().toString().trim(); 
        String ecgsex = ((RadioButton) NetRegister.this.findViewById(sexradio
                .getCheckedRadioButtonId())).getText().toString();
        JSONObject jsObject;

        try {
            jsObject = regadd(username, password, phone, tall, ecgsex, weihgt,age);
            if (jsObject.getString("username") != null)
                ;
            {
                Toast.makeText(NetRegister.this, "注册成功,", Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            /* DialogUtil.showDialog(this, "服务器响应失败，请重新再试！", false); */
            e.printStackTrace();
        }

        return false;
    }

    // --------------输入框正确性验证-------------
    private boolean validate()
    {
        String username = mEdituname.getText().toString().trim();
        String password = mEditupass1.getText().toString().trim();
        String repassword = mEditupass2.getText().toString().trim();

        String passwordval = "^[a-zA-Z0-9]{5,9}$";
        String strvalu1 = "^[a-zA-Z0-9@_\u4e00-\u9fa5]{1,12}$";//用户名正则验证汉字+英文

        String emailval = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean rs = false;
        boolean rs1 = false;

        boolean rs3 = false, rs4 = false, rs5 = false, rs6 = false, rs7 = false;

        rs = matcher(strvalu1, username);//用户名正则验证汉字+英文
        rs1 = matcher(emailval, username);

        rs3 = matcher(passwordval, password);

        if (username == null || username.length() < 1) {
            DialogUtil.showDialog(this, "用户名必填", false);
            return false;
        } else if (!rs) {
            DialogUtil.showDialog(this, "用户名不合法", false);
            return false;
        }else if (password == null || password.length() < 1) {
            DialogUtil.showDialog(this, "密码不能为空", false);
            return false;

        }  else if (!repassword.equals(password)) {
            DialogUtil.showDialog(this, "两次密码不一致,重新输入", false);
            return false;
        }

        return true;
    }

    private boolean validateinfo() {
        String phone = mphone.getText().toString().trim();
        String phoneval = "((^(13|15|18|17)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        
        String ageString = mage.getText().toString().trim();
        String ageval =  "^[0-9]{15,18}$";
        boolean rs2 = false;
        boolean rs3 = false;
        rs3 = matcher(ageval, ageString);
        rs2 = matcher(phoneval, phone);
        if (phone == null || phone.length() < 1) {
            DialogUtil.showDialog(this, "手机号码不能为空", false);
            return false;
        } else if (!rs2) {
            DialogUtil.showDialog(this, "请输入正确的手机号码！", false);
            return false;
        }else if (ageString == null || ageString.length() < 1) {
            DialogUtil.showDialog(this, "身份号码不能为空", false);
            return false;
        }else if (!rs3) {
            DialogUtil.showDialog(this, "请输入正确身份证号码！", false);
            return false;
        }
        return true;

    }
    private boolean validateinfo1() {
        String phone = mphone.getText().toString().trim();
        String tall = mtall.getText().toString().trim();
        String weight = mweight.getText().toString().trim();
        String ageString = mage.getText().toString().trim();
        String tallval = "^[0-9]{2,3}$";
        String ageval =  "^[0-9]{15,18}$";
        String phoneval = "((^(13|15|18|17)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        boolean rs2 = false;
        boolean rs3 = false;
        boolean rs4 = false;
        boolean rs5 = false;
        rs2 = matcher(phoneval, phone);
        rs3 = matcher(tallval, tall);
        rs4 = matcher(tallval, weight);
        rs5 = matcher(ageval, ageString);
        if (phone == null || phone.length() < 1) {
            DialogUtil.showDialog(this, "手机号码不能为空", false);
            return false;
        } else if (!rs2) {
            DialogUtil.showDialog(this, "请输入正确的手机号码！", false);
            return false;
        }else if (!rs3) {
            DialogUtil.showDialog(this, "请输入正确身高！", false);
            return false;
        }else if (!rs4) {
            DialogUtil.showDialog(this, "请输入正确体重！", false);
            return false;
        }else if (!rs5) {
            DialogUtil.showDialog(this, "请输入正确身份证号码！", false);
            return false;
        }else if (ageString == null || ageString.length() < 1) {
            DialogUtil.showDialog(this, "身份证号码不能为空！", false);
        }
        return true;

    }
    

    private static boolean matcher(String reg, String string) {
        boolean tem = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }

    private JSONObject checknameurl(String username)
            throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        String url1 = HttpUtil.BASE_URL + "logorename.do?action=logouname";
        JSONObject jsonback1 = new JSONObject(HttpUtil.postRequest(url1, map));
        return jsonback1;
    }

    // -----------设置URL携带数据格式-------------
    @SuppressWarnings("unused")
    private JSONObject regadd(String username, String password, String phone, String tall,
            String ecgsex, String weihgt,String age)
            throws Exception
    {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("uname", username);
        map1.put("passwd", password);
        map1.put("phone", phone);
        map1.put("height", tall);
        map1.put("gender", ecgsex);
        map1.put("weight", weihgt);
        map1.put("age", age);
       // map1.put("iden", _miden);
        String url1 = HttpUtil.BASE_URL + "modifyuser.do?action=adduser";
        JSONObject regjsonback = new JSONObject(HttpUtil.postRequest(url1, map1));
        return regjsonback;
    }

    private void localreg() {
        String name = mEdituname.getText().toString().trim();
        String password = mEditupass1.getText().toString().trim();
        String phone = mphone.getText().toString().trim();
        String userHeight = mtall.getText().toString().trim();
        String useWeight = mweight.getText().toString().trim();
        String age = mage.getText().toString().trim();
        String sexstr = ((RadioButton) NetRegister.this.findViewById(sexradio
                .getCheckedRadioButtonId())).getText().toString();
        
       
        UserInfo user = new UserInfo();
        user.setUserName(name);
        user.setUserPwd(password);
        user.setPhone(phone);
        user.setUserSex(sexstr);
        user.setUserAge(oldString(age));
        user.setUserNum(age);
        user.setUserHeight(userHeight);
        user.setUseWeight(useWeight);

        mService.register(user);
        Intent intent = new Intent();
        intent.setClass(NetRegister.this
                , Welcome.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("password", password);
        bundle.putString("age", oldString(age));
        bundle.putString("tall", userHeight);
        bundle.putString("weight", useWeight);
        bundle.putString("sex", sexstr);
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
    }
  
    private String oldString (String ageold){
       
           
        if (ageold==null || ageold.length() < 0) {
            
          return null;
        }else {
            int idsub = Integer.parseInt(ageold.substring(6, 10));
            
            
            int datody = Integer.parseInt(format.format(today));
           
            String ageString = String.valueOf(datody - idsub);
            System.out.print("ddddd:"+datody);
           System.out.print("ddddd:"+datody);
           System.out.print("FFFFF:" + idsub);
            Log.i("ikii-", ageString);
            return ageString;
        }
          
            
        
    }
    private void back() {
        imagback = (ImageView) findViewById(R.id.netreg_imageview_goback);
        imagback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(NetRegister.this, Welcome.class);
                setResult(RESULT_CANCELED, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish();

            }
        });

    }

}
