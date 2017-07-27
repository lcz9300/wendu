package com.HWDTEMPT.hwdtempt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.tool.TxtReader;
import com.HWDTEMPT.view.History_Draw;
import com.HWDTEMPT.view.hisdraw_left;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class History extends Activity {

    private Handler mainHandler = new MsgHandler();
    //private TextView xinlv, Result,uname,utall,usex,uage,uweight;
    private TextView gntxtView;
    private ProgressDialog pd;
    private RelativeLayout linear,linerleft;
    private ImageView backimg;
    
    private static int scaleRate = 2;//10

private int[]ECGDraw,Rpoints,Qpoints,Q1points,Spoints,S2points,Ppoints,Tpoints;

private String Filename="",Heartbeatstring="",age,tall,sex,weight,gnString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        

        Intent intent = getIntent();
        Filename = intent.getStringExtra("ID");
        
        initer();
        
        if (Filename !=null) {
            try {
             //   File file2 = new File(Environment.getExternalStorageDirectory() + "//" + "BMDBT"+"//"+MainActivity.NAME, Filename + ".txt");
                File file2 = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB", "gain"+Filename + ".txt");
                gnString = TxtReader.GetString(file2).toString().trim();
                Log.e("1111", gnString);
               if (gnString.equals("10")) {
                   gntxtView.setText("走速:25mm/s   增益:"+gnString+"mm/mV");
                   scaleRate = 3;
                }else {
                    gntxtView.setText("走速:25mm/s   增益:"+"5"+"mm/mV");
                    scaleRate = 2;
                }
                
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                gntxtView.setText("走速:25mm/s   增益:"+"5"+"mm/mV");
            }
            
            
        }
        
        if (Filename != null) {
            pd = ProgressDialog.show(History.this, "解析", "数据正在解析中，请稍后");
       //     File file = new File(Environment.getExternalStorageDirectory() + "//" + "BMDBT", Filename + ".txt");
           File file = new File(Environment.getExternalStorageDirectory() + "//" + "BMDBT"+"//"+MainActivity.NAME, Filename + ".txt");      
            String ECG = TxtReader.GetString(file);
           
            ReadThread mReadThread = new ReadThread(ECG);
            mReadThread.start();
        } else {
            new AlertDialog.Builder(this).setTitle("错误").setMessage("读取数据失败，请重试").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    History.this.finish();
                }

            }).create();
        }

    }

    private void initer() {
        /*xinlv = (TextView) findViewById(R.id.history_xinlv);
        Result = (TextView) findViewById(R.id.history_Result);
        uname=(TextView)findViewById(R.id.history_uname);
        uage=(TextView)findViewById(R.id.history_uage);
        usex=(TextView)findViewById(R.id.history_usex);
        utall=(TextView)findViewById(R.id.history_utall);
        uweight=(TextView)findViewById(R.id.history_uweight);*/
        gntxtView = (TextView)findViewById(R.id.gainttxt);
        
        backimg = (ImageView)this.findViewById(R.id.ecghisback_imageview_gohomexueyang);
        backimg.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }


    class MsgHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            case 1: 

                pd.dismiss();
                SQLiteHelp mService = new SQLiteHelp(History.this);
                String sqlString = "select phone,Height,weight,age,sex from user where username =?";
                Cursor cursor = mService.select(sqlString, new String[]{MainActivity.NAME});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    tall = cursor.getString(cursor.getColumnIndex("Height"));
                    age = cursor.getString(cursor.getColumnIndex("age"));
                    sex = cursor.getString(cursor.getColumnIndex("sex"));
                    weight = cursor.getString(cursor.getColumnIndex("weight"));
                }
                
                /*xinlv.setText(Heartbeatstring);
                uname.setText(MainActivity.NAME+",");
                uage.setText(age + "岁"+" "+",");
                usex.setText(sex+",");
                utall.setText(tall + "CM"+",");
                uweight.setText(weight + "KG"+",");*/
                //UserInfo user =new UserInfo();
                //user.setUserAge(age);

                //xinlv.setText(Heartbeatstring);
                
                RelativeLayout linear= (RelativeLayout) findViewById(R.id.linear);
                linerleft = (RelativeLayout)findViewById(R.id.linewwwleft);
                linear.addView(new History_Draw(History.this,ECGDraw,Rpoints,Qpoints,Q1points,Spoints,S2points,Ppoints,Tpoints));
                linerleft.addView(new hisdraw_left(History.this,Filename));
                
                break;
            case 2:
                Toast.makeText(History.this, "本地数据库没有特征点记录",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ReadThread extends Thread {
        String ECG = "";
        
        private float egoRawData = 0;
       

        public ReadThread(String ECG) {
            this.ECG = ECG;
            
        }

        public void run() {

            String[] ECGdata = ECG.split(",");
            ECGDraw = new int[ECGdata.length];
            for (int i = 0; i < ECGdata.length; i++) {
                try {

                    ECGDraw[i]  = Integer.parseInt(ECGdata[i]) * scaleRate;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            
            
            
            //从数据库中读取数据
            
            SQLiteHelp uService = new SQLiteHelp(History.this);
            String sql = "select Rpoint,Qpoint,Q1point,Spoint,S2point,Ppoint,Tpoint,Heartbeat from Diagnosis where username=? and time=?"; 
            Cursor cursor = uService.select(sql,new String[]{MainActivity.NAME,Filename});
            
            if (cursor.getCount() > 0) 
            { 

            // 必须使用moveToFirst方法将记录指针移动到第1条记录的位置 
            cursor.moveToFirst(); 
            String[] Rpointstring = cursor.getString(cursor.getColumnIndex("Rpoint")).split(","); 
            String[] Qpointstring = cursor.getString(cursor.getColumnIndex("Qpoint")).split(",");
            String[] Q1pointstring = cursor.getString(cursor.getColumnIndex("Q1point")).split(",");
            String[] Spointstring = cursor.getString(cursor.getColumnIndex("Spoint")).split(",");
            String[] S2pointstring = cursor.getString(cursor.getColumnIndex("S2point")).split(",");
            String[] Ppointstring = cursor.getString(cursor.getColumnIndex("Ppoint")).split(",");
            String[] Tpointstring = cursor.getString(cursor.getColumnIndex("Tpoint")).split(",");
            Heartbeatstring = cursor.getString(cursor.getColumnIndex("Heartbeat"));
            
            
            Rpoints=new int[Rpointstring.length];
            for(int i=1;i<Rpointstring.length;i++)
            {
                try{
                Rpoints[i]=Integer.parseInt(Rpointstring[i]);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            Qpoints=new int[Qpointstring.length];
            for(int i=1;i<Qpointstring.length;i++)
            {
                Qpoints[i]=Integer.parseInt(Qpointstring[i]);
            }
            
            Q1points=new int[Q1pointstring.length];
            for(int i=1;i<Q1pointstring.length;i++)
            {
                Q1points[i]=Integer.parseInt(Q1pointstring[i]);
            }
            
            Spoints=new int[Spointstring.length];
            for(int i=1;i<Spointstring.length;i++)
            {
                Spoints[i]=Integer.parseInt(Spointstring[i]);
            }
            
            S2points=new int[S2pointstring.length];
            for(int i=1;i<S2pointstring.length;i++)
            {
                S2points[i]=Integer.parseInt(S2pointstring[i]);
            }
            
            Ppoints=new int[Ppointstring.length];
            for(int i=1;i<Ppointstring.length;i++)
            {
                Ppoints[i]=Integer.parseInt(Ppointstring[i]);
            }
            
            Tpoints=new int[Tpointstring.length];
            for(int i=1;i<Tpointstring.length;i++)
            {
                Tpoints[i]=Integer.parseInt(Tpointstring[i]);
            }
            
            } else
            {
                Rpoints=new int[]{0};
                Qpoints=new int[]{0};
                Q1points=new int[]{0};
                Spoints=new int[]{0};
                S2points=new int[]{0};
                Ppoints=new int[]{0};
                Tpoints=new int[]{0};
                
                Message msg = Message.obtain();
                msg.what = 2; 
                mainHandler.sendMessage(msg);
            }
            

            
            
            
            Message msg = Message.obtain();
            msg.what = 1; 
            mainHandler.sendMessage(msg);
        }

    }


 

}
