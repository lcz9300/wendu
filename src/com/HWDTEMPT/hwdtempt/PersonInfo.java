package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.tool.downuser;

import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PersonInfo extends Activity implements OnClickListener{
    
    private EditText phoneEd,tallEd,weightEd,ageEd,identityEd ;
    private RadioGroup radionGroup;
    private Button btnButtonok;
    private ImageView imagback;
    private String phone,tall,weight,age,sex,userNum;
    private ProgressDialog pd;
    private List<String> list = new ArrayList<String>();
    public static String NAMEIN;
    private static final int LISTINFO = 4;
    private static final int RESULTINFO =5;
    
    private Spinner spIdSpinner = null;  
    private ArrayAdapter<CharSequence> adapterIdinfo = null;  
    private static String[] IdInfo={"选择证件:","身份证","护照","驾驶证"};
    
    
    SQLiteHelp msql_ex = new SQLiteHelp(this);
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_poperson);
        
        Intent intent = getIntent();
        NAMEIN = intent.getStringExtra("username");
        inition();
        final List<String> Resul = msql_ex.selectuserinfo(this.NAMEIN);
        if (Resul.get(0)!=null) {
            phoneEd.setText(Resul.get(0));
            tallEd.setText(Resul.get(1));
            weightEd.setText(Resul.get(2));
            ageEd.setText(Resul.get(3));
          //  radionGroup.setText(Resul.get(0));
            identityEd.setText(Resul.get(5));
            Log.e("tag", "tag22222222222222222222");
            Log.e("tag234455555555555", Resul.get(0));
        }/*else {
            Log.e("tag", "tag11111111111111111111111");
            Message mg = new Message();
            mg.what = LISTINFO;
            myHandler.sendMessage(mg);
        }*/
       
    }
   
    private void inition(){
        phoneEd = (EditText)findViewById(R.id.register1_et_phone);
        tallEd = (EditText)findViewById(R.id.register1_et_tall);
        weightEd = (EditText)findViewById(R.id.register1_et_weght);
        ageEd = (EditText)findViewById(R.id.register1_et_old_old);
        identityEd = (EditText)findViewById(R.id.register1_et_old);
        radionGroup = (RadioGroup) findViewById(R.id.register1_radioGroup);
       
        
        this.spIdSpinner = (Spinner) super.findViewById(R.id.spinnerIdinfo);
        this.adapterIdinfo = new ArrayAdapter<CharSequence>(PersonInfo.this,
                R.layout.spinner_st,IdInfo); 
        /*this.adapterIdinfo = new ArrayAdapter<CharSequence>(PersonInfo.this,
                android.R.layout.simple_spinner_dropdown_item);*/
        this.adapterIdinfo.setDropDownViewResource(R.layout.drop_down_item);
        this.spIdSpinner.setAdapter(adapterIdinfo);
        this.spIdSpinner.setSelection(1);
        this.spIdSpinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
        
      
        
        
        btnButtonok = (Button)findViewById(R.id.register1_btnreg1);
        imagback = (ImageView)findViewById(R.id.ecg_imageview_gohome_info);
        btnButtonok.setOnClickListener(this);
        imagback.setOnClickListener(this);
      //phone,Height,weight,age,sex,userNum
        
      
      
        
        
    }
    

    private class OnItemSelectedListenerImpl implements OnItemSelectedListener {  
        @Override  
        public void onItemSelected(AdapterView<?> parent, View view,  
                int position, long id) {  
            String idString = parent.getItemAtPosition(position).toString();  
            Toast.makeText(PersonInfo.this, "选择是：" + idString,  
                    Toast.LENGTH_LONG).show();  
        }  
  
        @Override  
        public void onNothingSelected(AdapterView<?> parent) {  
            // TODO Auto-generated method stub  
        }  
  
    }  
    private Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    pd.dismiss();
                    DialogUtil.showDialog(PersonInfo.this, "超时，请检查网络", false);
                    break;
                case 1:
                    pd = ProgressDialog.show(PersonInfo.this, "提示", "请稍等...",false);
                    
                    JSONObject jsonObject;
                    boolean flag = downuser.updateuser(NAMEIN, phone, tall, weight, age, sex);
                    if (flag) {
                        Message msMessage = new Message();
                        msMessage.what = 3;
                        myHandler.sendMessage(msMessage);
                        
                    }
                    break;
                    
                case 3:
                    
                    pd.dismiss();
                    Toast.makeText(PersonInfo.this, "成功", Toast.LENGTH_SHORT).show();
                    break;
                case LISTINFO:
                    pd = ProgressDialog.show(PersonInfo.this, "提示", "请稍等...",false);
                    list = downuser.dowuserList(NAMEIN);
                    phoneEd.setText(list.get(0));
                    tallEd.setText(list.get(1));
                    weightEd.setText(list.get(2));
                    Message msMessage = new Message();
                    msMessage.what = RESULTINFO;
                    myHandler.sendMessage(msMessage);
                    break;
                case RESULTINFO:
                    pd.dismiss();
                    break;
                
                  
                }
            }
    };
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
   
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.register1_btnreg1:
                phone = phoneEd.getText().toString().trim();
                tall = tallEd.getText().toString().trim();
                weight = weightEd.getText().toString().trim();
                age = ageEd.getText().toString().trim();
                sex = ((RadioButton) PersonInfo.this.findViewById(radionGroup
                        .getCheckedRadioButtonId())).getText().toString();
                userNum = identityEd.getText().toString().trim();
                
               msql_ex.userinfoupdate(NAMEIN, phone, tall, weight, sex, userNum, age);
               Toast.makeText(PersonInfo.this, "修改成功",  
                       Toast.LENGTH_LONG).show();  
               /* 
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
                */
                break;
            case R.id.ecg_imageview_gohome_info:
                /*Intent intent1 = new Intent();
                intent1.setClass(PersonInfo.this, ChoiceHome.class);
                intent1.putExtra("username", NAMEIN);
                startActivity(intent1);*/
                this.finish();
                break;
            default:
                break;
        }
    }
}
