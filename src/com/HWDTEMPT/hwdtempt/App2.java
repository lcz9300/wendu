package com.HWDTEMPT.hwdtempt;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.HWDTEMPT.hwdtempt.R.color;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.view.CalendarView;
import com.HWDTEMPT.view.CalendarView.OnItemClickListener;
import com.HWDTEMPT.hwdtempt.App2;
import com.HWDTEMPT.hwdtempt.History;
import com.HWDTEMPT.hwdtempt.MainActivity;
import com.HWDTEMPT.hwdtempt.R;
import com.HWDTEMPT.hwdtempt.App2.btnListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class App2 extends Activity {
   
    public static boolean fenbianlvda;
    public static boolean fenbianlvxiao;
    public static boolean fenbianlvxiao2;
    public static boolean fenbianlvxiaoqita;
    static boolean flag = false;
    private int screenWidth,screenHeight;
    private CalendarView calendar;
    private ImageButton calendarLeft;
    private TextView calendarCenter;
    private ImageButton calendarRight;
    private SimpleDateFormat format,format2,format3;
     private long exitTime = 0;
     LinearLayout linear;
     String today="";

  List<String> history = new ArrayList<String>();
  SQLiteHelp uService = new SQLiteHelp(App2.this);
    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app2);

        DisplayMetrics  dm = new DisplayMetrics();    
        //取得窗口属性    
        getWindowManager().getDefaultDisplay().getMetrics(dm);    
           
        //窗口的宽度    
        screenWidth = dm.widthPixels;    
           
        //窗口高度    
        screenHeight = dm.heightPixels;   
        
        if (screenWidth > 1500) {
          fenbianlvda=true;
        }else if( screenWidth > 1400&&screenWidth<1500) {
            fenbianlvxiao=true;
        }else if( screenWidth > 1000&&screenWidth<1200) {
            fenbianlvxiao2=true;
        }else {
            fenbianlvxiaoqita=true;
        }
        
        format = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        format3 = new SimpleDateFormat("HH:mm:ss");

      
        //设置日历表

        calendar = (CalendarView)findViewById(R.id.calendar);    
        calendarLeft = (ImageButton)findViewById(R.id.calendarLeft);
        calendarCenter = (TextView)findViewById(R.id.calendarCenter);
        calendarRight = (ImageButton)findViewById(R.id.calendarRight);
  
        
        
   try {
            
            Date date = format.parse("2015-01-01");
            calendar.setCalendarData(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
   


   
   linear=(LinearLayout) findViewById(R.id.linearlayoutin);
   today=format.format(new Date()).replace("-", "");
   Addview(List_history(today));
   

        
        
        String[] ya = calendar.getYearAndmonth().split("-"); 
        calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
        calendarLeft.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            
                String leftYearAndmonth = calendar.clickLeftMonth(); 
                String[] ya = leftYearAndmonth.split("-"); 
                calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
            }
        });
        
        calendarRight.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            
                String rightYearAndmonth = calendar.clickRightMonth();
                String[] ya = rightYearAndmonth.split("-"); 
                calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
            }
        });
        

        calendar.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void OnItemClick(Date selectedStartDate,
                    Date selectedEndDate, Date downDate) {
                    today=format.format(downDate).replace("-", "");
                   refreshList(today);
            }
        });

    }
    
    
    
    private void Addview(List<String> list_history) {
        // TODO Auto-generated method stub
       if(list_history.size()==1 &&list_history.get(0)=="当日没有记录")
       {
           LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
           TextView text=new TextView(this);
           text.setText("当日没有记录");
           text.setTextSize(30);
           text.setLayoutParams(lp);
           linear.addView(text);
       }
       else
       {

        for(int i=0;i<list_history.size();i++)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LayoutInflater inflater = LayoutInflater.from(App2.this);
         View view = inflater.inflate(R.layout.history_list, null);
        view.setLayoutParams(lp);
         TextView Timetext = (TextView) view.findViewById(R.id.textView1);
         Timetext.setId(i);
         Timetext.setText(list_history.get(i));
         Button checkresult = (Button) view.findViewById(R.id.button1);
         checkresult.setId(i);
     //  Button checkview = (Button) view.findViewById(R.id.button2);
     //  checkview.setId(i+100);
         Button deleteview = (Button) view.findViewById(R.id.button3);
         deleteview.setId(i+10000);
         //给按钮添加监听事件
         checkresult.setOnClickListener(new btnListener(checkresult));
    //   checkview.setOnClickListener(new btnListener(checkview));
         deleteview.setOnClickListener(new btnListener(deleteview));
         linear.addView(view);
        }
        
      }
    }
    
    
    
    
    /*
     * 创建一个按钮监听器类, 作用是点击按钮后改变按钮的名字
     */
    class btnListener implements OnClickListener
    {
        //定义一个 Button 类型的变量
        private Button btn;
        
        /*
         * 一个构造方法, 将Button对象做为参数
         */
        private btnListener(Button btn)
        {
            this.btn = btn;//将引用变量传递给实体变量
        }
        public void onClick(View v)
        {

            
            if(btn.getId()>=10000)
            {
                
                
                new AlertDialog.Builder(App2.this).setTitle("警告").setMessage("确定删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String time= history.get(btn.getId()-10000).replace(":", ""); 
                        
                        
                        
                        
                        uService.deletevalue(MainActivity.NAME, today+time);
                        
                        File path = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB" + "//" + time.replace("-", "") + ".txt");
                        if (path.exists()) {
                            path.delete();
                        }
                        
                        refreshList(today);  
                    }
                }).setNegativeButton("取消", null).show();
                    
                 
                
 
                

                
            }else 
            {
                final String Time= history.get(btn.getId()).replace(":", ""); 
                
                LayoutInflater inflater = LayoutInflater.from(App2.this);
                final View textEntryView = inflater.inflate(R.layout.result_layout, null);
                final TextView resulttext0 = (TextView) textEntryView
                        .findViewById(R.id.textView_xueyaVal);
                    //查询时间精确到时分(十位分)，例如：20160813072==2016-08-13 07:2
                final  List<String> xueyaString = uService.wendu_sel(MainActivity.NAME, today + Time);
                   Log.e("tag",  today + Time.substring(0, 3));
                    if (xueyaString.size()> 0) {
                        Log.e("tag1", "msg");
                        //resulttext0.setText(xueyaString.get(0)+"/"+xueyaString.get(1));
                       float wendu=Float.parseFloat(xueyaString.get(0));
                       float huashiwendu=32+wendu*1.8f;
                       BigDecimal   b  =   new BigDecimal(huashiwendu);  
                       float huashi =b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
                       if (wendu<35.7f) {
                    	   resulttext0.setText(xueyaString.get(0)+"℃"+"   "+huashi+"℉"+"   "+"偏低");
					}else if (wendu>=35.7f&&wendu<=37.5f) {
						   resulttext0.setText(xueyaString.get(0)+"℃"+"   "+huashi+"℉"+"   "+"正常");
					}else if (wendu>37.5f) {
						 resulttext0.setText(xueyaString.get(0)+"℃"+"   "+huashi+"℉"+"   "+"偏高");
					}      
                    } else {
                        resulttext0.setText(00);
                        //Log.e("tag",  today + Time.substring(0, 4));
                    }
                    
                    
                final AlertDialog.Builder builder = new AlertDialog.Builder(App2.this);
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("温度值供参考");
                builder.setView(textEntryView);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                builder.show(); 
   
            }
           
            
        }
    }
    
    public String returnResult(String rate)
    {
        String result="";
        int number=Integer.parseInt(rate);
        if(number<50)
        {
            result="正常";
        }else
        {
            result="偏高";
        }
        return result;
    }

    private void refreshList(String Time) {
        // TODO Auto-generated method stub
        linear.removeAllViews();
        Addview(List_history(Time));
         //adapt.notifyDataSetChanged(); 
    }
    
    public List<String>List_history(String time)
    {
        history.clear();
       
        SQLiteHelp uService = new SQLiteHelp(App2.this);
        String sql = "select time from wendu where username=?and time like'"+time+"%'ORDER BY time ASC";
        Cursor cursor = uService.select(sql, new String[] {
            MainActivity.NAME
        });

        if (cursor.getCount() > 0)
        {
           flag=true;
            while (cursor.moveToNext())
            {
                
                try{
                    Date date = format2.parse(cursor.getString(cursor.getColumnIndex("time")));

                    String s =  format3.format(date);
                    history.add(s);
                }catch(ParseException e)
                {

                e.printStackTrace();

                } 
              
            }

        }

        else
        {
            flag=false;
            history.add("当日没有记录");
        }
        cursor.close();
        return history;
    }
    
    

    

    
    

    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(today!="")
        {
        refreshList(today);
        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {  
    
           if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
           {  
               if((System.currentTimeMillis()-exitTime) > 2000){  
                   Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
                   exitTime = System.currentTimeMillis();  
               } else {  
                   finish();
                    System.exit(0);
               }  
               return true;  
           }  
           return super.onKeyDown(keyCode, event);  
        }
  
}

