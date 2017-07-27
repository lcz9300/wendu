
package com.HWDTEMPT.hwdtempt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.HWDTEMPT.hwdtempt.R.color;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.view.Bloodhistory;
import com.HWDTEMPT.view.HomeDiagram;
import com.HWDTEMPT.view.wenduhomediagram;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.R.string;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;

public class bdlishiactivity extends Activity implements OnClickListener {
    
    public static boolean fanhuixy=false;
    long exitTime=0;
    RelativeLayout linear0;//图表界面
    private TextView TextA, TextB, TextC, TextD;//周月季年
    private SimpleDateFormat format,format2;//时间格式化
    SQLiteHelp uService = new SQLiteHelp(bdlishiactivity.this);//数据库
    HashMap<Long, String> lists0 = new HashMap<Long, String>();
  
    final int XINLV = 0, FANGXINGZAOBO = 1, SHIXINGZAOBO = 2, JIAOJIEXINGZAOBO = 3, XINLVBUQI = 4,
            SHIXINGXINDONGGUOSU = 5, FANGXINGYIBO = 6, SHIXINGYIBO = 7, FANGCHAN = 8,XUEYA=9;
//    final float wendu=10;
    final int DAY = 0, ZHOU = 1, YUE = 2, YEAR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xueyalishijilu);
        initViews();
        setTabSelection(0, DAY);
    }

    private void initViews() {
        format = new SimpleDateFormat("yyyyMMddHHmmss");
        format2 = new SimpleDateFormat("yyyyMMdd");
        TextA = (TextView) findViewById(R.id.ri_text);
        TextB = (TextView) findViewById(R.id.zhou_text);
        TextC = (TextView) findViewById(R.id.yue_text);
     //   TextD = (TextView) findViewById(R.id.year_text);
        linear0 = (RelativeLayout) findViewById(R.id.linear0);
      

        TextA.setOnClickListener(this);
        TextB.setOnClickListener(this);
        TextC.setOnClickListener(this);
    //    TextD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ri_text:
                setTabSelection(0,DAY);
                break;
            case R.id.zhou_text:
                setTabSelection(1, ZHOU);
                break;
            case R.id.yue_text:
                setTabSelection(2, YUE);
                break;
/*            case R.id.year_text:
                setTabSelection(3, YEAR);
                break;*/
            default:
                break;
        }
    }
    
    
/*
 * 点击周月季年中的某一个所使用的方法
 */
    private void setTabSelection(int index, int currenttime) {

        clearSelection();//清除所有界面的图表

        switch (index) {
            case 0:
                TextA.setTextColor(color.qianlvse);
                TextA.setBackground(getResources().getDrawable(R.drawable.whilt9));
                RefreshListData(0);//刷新数据
                break;
            case 1:
                TextB.setTextColor(color.qianlvse);
                TextB.setBackground(getResources().getDrawable(R.drawable.whilt9));
                RefreshListData(1);
                break;

            case 2:
                TextC.setTextColor(color.qianlvse);
                TextC.setBackground(getResources().getDrawable(R.drawable.whilt9));
                RefreshListData(2);
                break;

   /*         case 3:
                TextD.setTextColor(color.qianlvse);
                TextD.setBackground(getResources().getDrawable(R.drawable.whilt9));
                RefreshListData(3);
                break;*/
        }
        
        linear0.addView(new wenduhomediagram(this, lists0, XUEYA, currenttime));
        
     //   linear0.addView(new Bloodhistory(this, lists0, XUEYA, currenttime));

    }

    
    
    /*
     * 刷新数据，重新从数据库中获取
     */
    private void RefreshListData(int index) {
        // TODO Auto-generated method stub
        lists0.clear(); 
        Date dateNow = new Date();
        Date dateFrom = new Date();
        long past = 0, now = 0,past2 = 0, now2 =0;
        //血压用
        Calendar clCalendar = Calendar.getInstance();
        String sqlString = "";
        Cursor cursorsCu = null;
        //其他用
        Calendar cl = Calendar.getInstance();
        String sql = "";
        String sqlwendu = ""; 
        Cursor cursor = null;
        switch (index) {
            case 0:
                cl.setTime(dateNow);
                
                cl.add(Calendar.DAY_OF_MONTH, -7); // 一周
                dateFrom = cl.getTime();
                past = Long.parseLong(format.format(dateFrom));
                now = Long.parseLong(format.format(dateNow));
                sql = "select * from wendu where username=? and time between '" + past + "' and '"
                        + now + "' ORDER BY time ASC ";
                cursor = uService.select(sql, new String[] {
                    MainActivity.NAME
                    
                });
                
                ////------------------------血压查询结束--------------------------------
                break;
            case 1:
          cl.setTime(dateNow);
                
                cl.add(Calendar.DAY_OF_MONTH, -7); // 一月
                dateFrom = cl.getTime();
                past = Long.parseLong(format.format(dateFrom));
                now = Long.parseLong(format.format(dateNow));
                sql = "select * from wendu where username=? and time between '" + past + "' and '"
                        + now + "' ORDER BY time ASC ";
                cursor = uService.select(sql, new String[] {
                    MainActivity.NAME
                    
                });
                break;
            case 2:
                
                cl.setTime(dateNow);
                
                cl.add(Calendar.DAY_OF_MONTH, -7); // 一日
                dateFrom = cl.getTime();
                past = Long.parseLong(format.format(dateFrom));
                now = Long.parseLong(format.format(dateNow));
                sql = "select * from wendu where username=? and time between '" + past + "' and '"
                        + now + "' ORDER BY time ASC ";
                cursor = uService.select(sql, new String[] {
                    MainActivity.NAME
                    
                });  
       

            default:
                break;
        }
        
       
        if (cursor.getCount() > 0)
        {

            while (cursor.moveToNext())
            {

                String wendu=cursor.getString(cursor.getColumnIndex("wenduvalue"));
                long date = Long.parseLong(cursor.getString(cursor.getColumnIndex("time")));
            //    lists0.put(date, wendu);
                Log.e("ssssssssssssssssss", wendu+"");
             // System.out.print(wendu+"ssssssssssssss");

            }

        }
        cursor.close();
  

    }
    private void CheckBloodValue(){
        String sql1 = "";
        Cursor cursor1 = null;
    }
    private void clearSelection() {
        linear0.removeAllViews();

     
        TextA.setTextColor(Color.WHITE);
        TextB.setTextColor(Color.WHITE);
        TextC.setTextColor(Color.WHITE);
    //    TextD.setTextColor(Color.WHITE);
        TextA.setBackground(getResources().getDrawable(R.drawable.txt123));
        TextB.setBackground(getResources().getDrawable(R.drawable.txt123));
        TextC.setBackground(getResources().getDrawable(R.drawable.txt123));
    //    TextD.setBackground(getResources().getDrawable(R.drawable.txt123));

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {  
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
                exitTime = System.currentTimeMillis();  
            } else {  
                fanhuixy=true;
                finish();
                // System.exit(0);
            }  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
     }

}