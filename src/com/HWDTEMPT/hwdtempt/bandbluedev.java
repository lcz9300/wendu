
package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.Util.ClsUtils;

import java.lang.reflect.Method;

public class bandbluedev extends Activity implements OnClickListener
{
    public static String mshebeiflag;
    public static String NAMEB;
    public static String BIAOZHI;
    private EditText hwdtype,hwdname,hwdid;
    private Editor editor;
    private Button btncommit;
    private SharedPreferences spPreferences;
   //--����ѡ���
    private boolean firstFlag = true;
    private Spinner spinner = null;
    private ArrayAdapter<CharSequence> adapterIdinfo = null;
    private static String[] IdInfo={"�����ĵ���:HWD-OB01","Ѫѹ�ĵ�:HWD-BPM01","�����:HWD-MBSS01"};
    //����
    private ImageView backimag;
    private String hwdtype_str,hwdname_str,hwdid_str;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baund);
        Intent intent = getIntent();
        mshebeiflag = intent.getStringExtra("flag");
        NAMEB = intent.getStringExtra("username");
       // BIAOZHI = intent.getStringExtra("hwdtype");
        spPreferences = getSharedPreferences("hwdinfo", MODE_PRIVATE);
        editor = spPreferences.edit();
        init();
      
    }
    private void init(){
        
        hwdtype = (EditText)this.findViewById(R.id.band_et_hwdtype);
        hwdname = (EditText)this.findViewById(R.id.band_et_hwdname);
        hwdid = (EditText)this.findViewById(R.id.band_et_hwdid);
        
        btncommit = (Button)this.findViewById(R.id.band_et_hwdcommit);
        backimag = (ImageView)this.findViewById(R.id.band_imageview_gohomeband);
        
       
        
        this.spinner = (Spinner)super.findViewById(R.id.spinnerIdinfo_band);
        this.adapterIdinfo = new ArrayAdapter<CharSequence>(bandbluedev.this,
                R.layout.spinner_st,IdInfo); 
        this.adapterIdinfo.setDropDownViewResource(R.layout.drop_down_item);
        this.spinner.setAdapter(adapterIdinfo);
       
        this.spinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
        
        Log.i("tag", "111111111111");
        hwdtype.setText(spPreferences.getString("type", ""));
        hwdname.setText(spPreferences.getString("name", ""));
        Log.i("tag", spPreferences.getString("type", ""));
      
      
       
        btncommit.setOnClickListener(this);
        backimag.setOnClickListener(this);
    }
    private class OnItemSelectedListenerImpl implements OnItemSelectedListener {  
        @Override  
        public void onItemSelected(AdapterView<?> parent, View view,  
                int position, long id) {  
            String idString = parent.getItemAtPosition(position).toString();  
            String selectdev = idString.split(":")[1];
            
            hwdtype.setText(selectdev);
             
            
            Toast.makeText(bandbluedev.this, "ѡ���ǣ�" + idString,  
                    Toast.LENGTH_LONG).show();  
            
        }  
  
        @Override  
        public void onNothingSelected(AdapterView<?> parent) {  
            // TODO Auto-generated method stub 
           
        }  
  
    }  
    private boolean panduan(){
        hwdtype_str = hwdtype.getText().toString().trim();
       
        if (hwdtype_str!=null) {
            return true;
       
        }else {
            return false;
        }
        
        
    }
    private boolean spboolen(){
        //spPreferences = getSharedPreferences("hwdinfo", MODE_PRIVATE);
        String typestr = spPreferences.getString("type", "");
        if (typestr!=null) {
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.band_imageview_gohomeband:
                if (spboolen()) {
                    Intent iniIntent = new Intent();
                    iniIntent.setClass(bandbluedev.this, MainActivity.class);
                    iniIntent.putExtra("username", NAMEB);
                    startActivity(iniIntent);
                    this.finish();
                }else {
                    DialogUtil.showDialog(bandbluedev.this, "�豸�ͺ�Ϊ�գ��������ύ��", false);
                }
                 
                break;
            case R.id.band_et_hwdcommit:
                if (panduan()) {
                    
                   String type_str = hwdtype.getText().toString();
                  
                    String did_str= hwdid.getText().toString();
                    if (type_str.equals("HWD-BPM01")) {
                        hwdname.setText("�ĵ�Ѫѹ��");
                    }else if (type_str.equals("HWD-OB01")) {
                        hwdname.setText("�ĵ���");
                    }else if (type_str.equals("HWD-MBSS01")) {
                        hwdname.setText("������໤��");
                    }
                    String hname_str = hwdname.getText().toString();
                    editor.putString("type", type_str);
                    editor.putString("name", hname_str);
                    editor.putString("id", did_str);
                    editor.commit();
                    Toast.makeText(bandbluedev.this, "������", Toast.LENGTH_SHORT).show();
                    
                        Intent inisp = new Intent();
                        inisp.setClass(bandbluedev.this, MainActivity.class);
                        inisp.putExtra("username", NAMEB);
                        startActivity(inisp);
                        this.finish();
                  
                }else {
                    DialogUtil.showDialog(bandbluedev.this, "�豸�ͺŲ���Ϊ��", false);
                }
                break;
            default:
                break;
        }
    }
}
