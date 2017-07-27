package com.HWDTEMPT.hwdtempt;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.HWDTEMPT.hwdtempt.R.color;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.view.CalendarView;
import com.HWDTEMPT.view.CalendarView.OnItemClickListener;

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


public class bloodrecord extends Activity {
   
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
  SQLiteHelp uService = new SQLiteHelp(bloodrecord.this);
    @Override   
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodreport);

        DisplayMetrics  dm = new DisplayMetrics();    
        //ȡ�ô�������    
        getWindowManager().getDefaultDisplay().getMetrics(dm);    
           
        //���ڵĿ��    
        screenWidth = dm.widthPixels;    
           
        //���ڸ߶�    
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
        format2 = new SimpleDateFormat("yyyyMMddHHmm");
        format3 = new SimpleDateFormat("HH:mm");

      
        //����������

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
        calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
        calendarLeft.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            
                String leftYearAndmonth = calendar.clickLeftMonth(); 
                String[] ya = leftYearAndmonth.split("-"); 
                calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
            }
        });
        
        calendarRight.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            
                String rightYearAndmonth = calendar.clickRightMonth();
                String[] ya = rightYearAndmonth.split("-"); 
                calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
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
       if(list_history.size()==1 &&list_history.get(0)=="����û�м�¼")
       {
           LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
           TextView text=new TextView(this);
           text.setText("����û�м�¼");
           text.setTextSize(30);
           text.setLayoutParams(lp);
           linear.addView(text);
       }
       else
       {

        for(int i=0;i<list_history.size();i++)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LayoutInflater inflater = LayoutInflater.from(bloodrecord.this);
         View view = inflater.inflate(R.layout.history_list, null);
        view.setLayoutParams(lp);
         TextView Timetext = (TextView) view.findViewById(R.id.textView1);
         Timetext.setId(i);
         Timetext.setText(list_history.get(i));
         Button checkresult = (Button) view.findViewById(R.id.button1);
         checkresult.setId(i);
      //   Button checkview = (Button) view.findViewById(R.id.button2);
      //   checkview.setId(i+100);
         Button deleteview = (Button) view.findViewById(R.id.button3);
         deleteview.setId(i+10000);
         //����ť��Ӽ����¼�
         checkresult.setOnClickListener(new btnListener(checkresult));
       //  checkview.setOnClickListener(new btnListener(checkview));
         deleteview.setOnClickListener(new btnListener(deleteview));
         linear.addView(view);
        }
        
      }
    }
    
    
    
    
    /*
     * ����һ����ť��������, �����ǵ����ť��ı䰴ť������
     */
    class btnListener implements OnClickListener
    {
        //����һ�� Button ���͵ı���
        private Button btn;
        
        /*
         * һ�����췽��, ��Button������Ϊ����
         */
        private btnListener(Button btn)
        {
            this.btn = btn;//�����ñ������ݸ�ʵ�����
        }
        public void onClick(View v)
        {

            
            if(btn.getId()>=10000)
            {
                
                
                new AlertDialog.Builder(bloodrecord.this).setTitle("����").setMessage("ȷ��ɾ����").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    
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
                }).setNegativeButton("ȡ��", null).show();
                    
                 
                
 
                

                
            }else if(btn.getId()>=100&& btn.getId()<10000)
            {
                 String time= history.get(btn.getId()-100).replace(":", ""); 
                
                Intent intent=new Intent();
              intent.putExtra("ID", today+time);
              intent.setClass(bloodrecord.this, History.class);
              startActivity(intent);
                

                
            }else 
            {
                final String Time= history.get(btn.getId()).replace(":", ""); 
                
                LayoutInflater inflater = LayoutInflater.from(bloodrecord.this);
                final View textEntryView = inflater.inflate(R.layout.result_layout, null);
                final TextView heartbeat = (TextView) textEntryView.findViewById(R.id.textView1);
                final TextView resulttext0 = (TextView) textEntryView
                        .findViewById(R.id.textView_xueyaVal);
         /*       final TextView resulttext1 = (TextView) textEntryView.findViewById(R.id.textView3);
                final TextView resulttext2 = (TextView) textEntryView.findViewById(R.id.textView5);
                final TextView resulttext3 = (TextView) textEntryView.findViewById(R.id.textView7);
                final TextView resulttext4 = (TextView) textEntryView.findViewById(R.id.textView9);
                final TextView resulttext5 = (TextView) textEntryView.findViewById(R.id.textView11);
                final TextView resulttext6 = (TextView) textEntryView.findViewById(R.id.textView13);
                final TextView resulttext7 = (TextView) textEntryView.findViewById(R.id.textView15);
                final TextView resulttext8 = (TextView) textEntryView.findViewById(R.id.textView17);
                final TextView resulttext9 = (TextView) textEntryView.findViewById(R.id.textView19);
                final TextView resulttext10 = (TextView) textEntryView
                        .findViewById(R.id.textView21);

                final TextView resulttext11 = (TextView) textEntryView.findViewById(R.id.textView4);
                final TextView resulttext12 = (TextView) textEntryView.findViewById(R.id.textView6);
                final TextView resulttext13 = (TextView) textEntryView.findViewById(R.id.textView8);
                final TextView resulttext14 = (TextView) textEntryView
                        .findViewById(R.id.textView10);
                final TextView resulttext15 = (TextView) textEntryView
                        .findViewById(R.id.textView12);
                final TextView resulttext16 = (TextView) textEntryView
                        .findViewById(R.id.textView14);
                final TextView resulttext17 = (TextView) textEntryView
                        .findViewById(R.id.textView16);
                final TextView resulttext18 = (TextView) textEntryView
                        .findViewById(R.id.textView18);
                final TextView resulttext19 = (TextView) textEntryView
                        .findViewById(R.id.textView20);
                final TextView resulttext20 = (TextView) textEntryView
                        .findViewById(R.id.textView22);
*/
            /*    final List<String> Result = uService
                        .FindfromSQLite(MainActivity.NAME, today + Time);
                heartbeat.setText(Result.get(Result.size() - 1));
                resulttext1.setText(Result.get(0)+"%");
                resulttext2.setText(Result.get(1)+"%");
                resulttext3.setText(Result.get(2)+"%");
                resulttext4.setText(Result.get(3)+"%");
                resulttext5.setText(Result.get(4)+"%");
                resulttext6.setText(Result.get(5)+"%");
                resulttext7.setText(Result.get(6)+"%");
                resulttext8.setText(Result.get(7)+"%");
                resulttext9.setText(Result.get(8)+"%");
                resulttext10.setText(Result.get(9)+"%");

                resulttext11.setText(returnResult(Result.get(0)));
                resulttext12.setText(returnResult(Result.get(1)));
                resulttext13.setText(returnResult(Result.get(2)));
                resulttext14.setText(returnResult(Result.get(3)));
                resulttext15.setText(returnResult(Result.get(4)));
                resulttext16.setText(returnResult(Result.get(5)));
                resulttext17.setText(returnResult(Result.get(6)));
                resulttext18.setText(returnResult(Result.get(7)));
                resulttext19.setText(returnResult(Result.get(8)));
                resulttext20.setText(returnResult(Result.get(9)));*/

             
                    //��ѯʱ�侫ȷ��ʱ��(ʮλ��)�����磺20160813072==2016-08-13 07:2
                final  List<String> xueyaString = uService.blood_sel(MainActivity.NAME, today + Time.substring(0, 3));
                   Log.e("tag",  today + Time.substring(0, 3));
                    if (xueyaString.size()> 0) {
                        Log.e("tag1", "msg");
                        //resulttext0.setText(xueyaString.get(0)+"/"+xueyaString.get(1));
                        resulttext0.setText(xueyaString.get(0)+"/"+xueyaString.get(1));
                       
                    } else {
                        resulttext0.setText("--/--");
                        //Log.e("tag",  today + Time.substring(0, 4));
                    }
                    
                    
                final AlertDialog.Builder builder = new AlertDialog.Builder(bloodrecord.this);
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("��������ο�");
                builder.setView(textEntryView);
                builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
            result="����";
        }else
        {
            result="ƫ��";
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
       
        SQLiteHelp uService = new SQLiteHelp(bloodrecord.this);
        String sql = "select time from Diagnosis where username=?and time like'"+time+"%'ORDER BY time ASC";
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
            history.add("����û�м�¼");
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
                   Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();  
                   exitTime = System.currentTimeMillis();  
               } else {  
                   finish();
              //      System.exit(0);
               }  
               return true;  
           }  
           return super.onKeyDown(keyCode, event);  
        }
  
}
