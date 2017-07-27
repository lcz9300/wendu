
package com.HWDTEMPT.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import com.HWDTEMPT.hwdtempt.MainActivity;
import com.HWDTEMPT.hwdtempt.R;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.StaticValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@SuppressLint("SimpleDateFormat")
public class HomeDiagram extends View {

    
    private HashMap<Long, Integer> milliliter;
  /*  private long daydayTime;
    private long tarTime;*/
    private int flagtag;
    private int tabtag;
    private float interval_left_right;
    private int interval_left=20;//左边的空白
    private int interval_kedu=20;//刻度长度
    private int interval_sum=0;//间隔个数
    private int buttom = 60, top = 50;//底部和顶部的留白
    private Paint paint_percent, paint_text, paintxy_text,paintXYaxis, keduPaint, framPanint,paintdata,
            shuxianPaint,blPaint;
    private Shader mShader;
    private Bitmap bitmap_point;
    private int paint_percent_size=40;
    private  List<String> listxt = new ArrayList<String>();
   
    private SQLiteHelp uService = new SQLiteHelp(HomeDiagram.this.getContext());//数据库
    public HomeDiagram(Context context, HashMap<Long, Integer> milliliter, int flagtag, int tabtag) {

        super(context);
        init(milliliter, tabtag);
        this.flagtag = flagtag;
        this.tabtag = tabtag;

    }

    public void init(HashMap<Long, Integer> milliliter, int tabtag) {

        this.milliliter = milliliter;
    
        // 数据画笔
        paint_percent = new Paint();
        paint_percent.setStrokeWidth(2);
        paint_percent.setTextSize(paint_percent_size);
        paint_percent.setColor(Color.BLACK);

        // 竖线
        shuxianPaint = new Paint();
        shuxianPaint.setStyle(Paint.Style.STROKE);
        shuxianPaint.setColor(Color.GRAY);
        shuxianPaint.setStrokeWidth(1);

        // 刻度
        keduPaint = new Paint();
        keduPaint.setStyle(Paint.Style.STROKE);
        keduPaint.setColor(Color.RED);
        keduPaint.setStrokeWidth(5);
        //xueya
        blPaint = new Paint();
        blPaint.setStyle(Paint.Style.STROKE);
        blPaint.setColor(Color.BLUE);
        blPaint.setStrokeWidth(5);

        // xy轴
        paintXYaxis = new Paint();
        paintXYaxis.setStyle(Paint.Style.STROKE);
        paintXYaxis.setColor(Color.RED);
        paintXYaxis.setStrokeWidth(5);

        // 渐变色
        framPanint = new Paint();
        framPanint.setAntiAlias(true);
        framPanint.setStrokeWidth(2f);
        // 画字体
        paint_text = new Paint();
        paint_text.setStrokeWidth(5);
        paint_text.setTextSize(50);
        paint_text.setColor(Color.BLACK);
        
        paintxy_text = new Paint();
        paintxy_text.setStrokeWidth(5);
        paintxy_text.setTextSize(40);
        paintxy_text.setColor(Color.BLACK);   
        
        
        paintdata = new Paint();
        paintdata.setStyle(Paint.Style.STROKE);
        paintdata.setColor(Color.RED);
        paintdata.setStrokeWidth(3);

        bitmap_point = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_point_blue);
        switch (tabtag)
        {
            case 0:
                interval_sum=7;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left*4) /7;
                setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth,LayoutParams.MATCH_PARENT));
                break;
            case 1:
                interval_sum=6;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left) / 4;// 5天一竖线，总共6根
                setLayoutParams(new LayoutParams((int) (interval_left_right*6+100),LayoutParams.MATCH_PARENT));
                break;
            case 2:
                interval_sum=9;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left) / 6;// 10天一竖线，总共9根
                setLayoutParams(new LayoutParams((int) (interval_left_right*9+100),LayoutParams.MATCH_PARENT));
                break;
            case 3:
                interval_sum=12;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left) /6;// 一月一竖线，总共12根
                setLayoutParams(new LayoutParams((int) (interval_left_right*12+100),LayoutParams.MATCH_PARENT));
                break;
            default:
                break;

        }
        

    }

    protected void onDraw(Canvas c) {
        
        drawGrid(c);
        drawBrokenLine(c);
        drawText(c);

    }

    // 画网格
    private void drawGrid(Canvas c) {
        
      
        
        // XY轴
        Path pathXYaxis = new Path();
        pathXYaxis.moveTo(interval_left, top/4);
        pathXYaxis.lineTo(interval_left, getHeight() - buttom);
        pathXYaxis.lineTo(getWidth(), getHeight() - buttom);
        c.drawPath(pathXYaxis, paintXYaxis);
        c.drawLine(interval_left, top/4, interval_left-16, top/4+30, paintXYaxis);//X轴箭头。   
        c.drawLine(interval_left, top/4, interval_left+16, top/4+30, paintXYaxis); 
        c.drawLine(getWidth(), getHeight() - buttom, getWidth()-30, getHeight() - buttom-16, paintXYaxis);//Y轴箭头。   
        c.drawLine(getWidth(), getHeight() - buttom, getWidth()-30, getHeight() - buttom+16, paintXYaxis); 

        // 画横线刻度
        float interval = (getHeight() - buttom - top) / 10;
        for (int j = 0; j < 11; j++) {
            if (j == 4)
            {
                // 横向虚线
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setStyle(Style.STROKE);
                p.setColor(Color.GRAY);
                p.setStrokeWidth(1);
                PathEffect effects = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p.setPathEffect(effects);
                c.drawLine(interval_left, getHeight() - buttom - interval * j,  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j, p);
            }

            if (j == 7)
            {
                // 横向虚线 2
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setStyle(Style.STROKE);
                p.setColor(Color.RED);
                p.setStrokeWidth(1);
                PathEffect effects = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p.setPathEffect(effects);
                c.drawLine(interval_left, getHeight() - buttom - interval * j,  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j, p);

            }
            
            if (j == 10)
            {
                // 横向虚线 2
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setStyle(Style.STROKE);
                p.setColor(Color.RED);
                p.setStrokeWidth(1);
                PathEffect effects = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p.setPathEffect(effects);
                c.drawLine(interval_left, getHeight() - buttom - interval * j,  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j, p);

            }
            
            
            c.drawLine(interval_left, getHeight() - buttom - interval * j, interval_kedu+interval_left, getHeight() - buttom - interval
                    * j, keduPaint);// 画y轴上面的刻度
        }

        // 画竖线+下标+具体值

        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date dateNow = new Date();
        Calendar cl = Calendar.getInstance();
        Iterator iter = milliliter.entrySet().iterator();
        
        Path pathrealdata=new Path();
        int rate=(getHeight() - buttom - top)/100;
        float x1=0,y1=0,yy1=0,yy2=0,xx1=0,xx2=0;
        
        //--------------------------------------血压有关开始------------------
        float xueyag_float_y1 = 0f;
        float xueyag_float_y2 = 0f;
        
        float xueyad_float_y1 = 0f;
        float xueyad_float_y2 = 0f;
        float xaaa1 = 0f;
        Path xueyadaoPath = new Path();//高压路径
        Path xueyadiPath = new Path();//低压路径
        
        float rate1=(getHeight() - buttom - top)/200f;
       
        
        //--------------------------------------血压有关结束--------------------
        
        switch (tabtag)
        {
            case 0:

                String Weekday[] = new String[8];
                for (int i = 0; i < 8; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, i - 7);
                    Weekday[i] = format.format(cl.getTime());

                }
                c.drawText(Weekday[0], 0, getHeight() - buttom / 4, paint_percent);
                for (int i = 1; i <= 7; i++)
                {
                    c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                            * i + interval_left, getHeight()
                            - buttom, shuxianPaint);
                    c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom
                            - interval_kedu,
                            interval_left_right * i + interval_left, getHeight() - buttom,
                            keduPaint);// 画x轴上面的刻度
                    c.drawText(Weekday[i], interval_left_right * i - paint_percent_size,
                            getHeight() - buttom / 4, paint_percent); // 画日期

                }

                // 画具体值

                long Weekdayint[] = new long[8];

                for (int i = 0; i < 8; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, i - 7);
                    Weekdayint[i] = Long.parseLong(format1.format(cl.getTime()).replace("-", ""));
                }

                while (iter.hasNext()) {

                    Map.Entry entry = (Map.Entry) iter.next();

                    long time = Long.parseLong(entry.getKey().toString()
                            .substring(0, entry.getKey().toString().length() - 2));

                    String time1=entry.getKey().toString()
                            .substring(0, entry.getKey().toString().length() - 2);
                    Log.e("LOL1124556", time1);
                    String value1=entry.getValue().toString();
                    
                    List<String> fl = new ArrayList<String>();    // 泛型，这ArrayList只能放Float类型
                    fl.add(value1);
                    String[] f = new String[fl.size()];
                    for (int s = 0; s < fl.size(); s++) {
                        f[s] = fl.get(s);
                   //e("LOL1124556", f[0]);
                     // Log.e("LOL1124556", f[8]);
                    }
             /*       yy1 = getHeight() - buttom - rate
                            *  Float.parseFloat(f[6]);
                    yy2 = getHeight() - buttom - rate
                            *  Float.parseFloat(f[8]);*/
          /*          List<String> f2 = new ArrayList<String>();    // 泛型，这ArrayList只能放Float类型
                    f2.add(time1);
                    String[] ff = new String[f2.size()];
                    for (int s = 0; s < f2.size(); s++) {
                        ff[s] = f2.get(s);
                        Log.e("LOL1124556", ff[s]);
                    }*/
                //    System.out.println(f+"ssssssssssssss");
               //     Log.e("LOL1124556", value1);
                 //   System.out.print(time);
                    
                    for (int i = 0; i < Weekdayint.length - 1; i++)
                    {
                        if (time <= Weekdayint[i + 1] && time >= Weekdayint[i])
                        {
                         //   Log.e("eeeeeeeeeeee", "2222222222222");
                            if (x1 == 0 && y1 == 0)
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.moveTo(x1, y1);
                            } else
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.lineTo(x1, y1);
                            }
                            c.drawBitmap(bitmap_point,
                                    x1 - bitmap_point.getWidth() / 2,
                                    y1 - bitmap_point.getHeight() / 2, null);
                         /*   c.drawLine( x1, y1 ,  x1 ,
                                    y1, keduPaint);*/
                            break;
                        }
              /*          List<Float> fl = new ArrayList<Float>();    // 泛型，这ArrayList只能放Float类型
                        fl.add(x1);
                        Float[] f = new Float[fl.size()];
                        for (int s = 0; s < fl.size(); s++) {
                            f[s] = fl.get(s);
                        }*/
                    //    System.out.println(f+"ssssssssssssss");
                    }
                }

                // c.drawPath(pathrealdata, paintdata);

                // -----------------------------------------------------------血压开始--------------------------------------------------

                if (flagtag == 9)
                {
                   
                    String Weekdayxueya[] = new String[8];
                    for (int i = 0; i < 8; i++) {
                        
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, i - 7);
                        Weekdayxueya[i] = format2.format(cl.getTime());
                       System.out.print("mmmmmmm"+Weekdayxueya[i]);
                        final List<String> li = uService.Findblood(Weekdayxueya, MainActivity.NAME,
                                i);
                        
                        if (li.size() > 0) 
                        {
                            if (Weekdayxueya[i].equals(li.get(2).substring(0, 8)))
                            { 
                                Log.e("111", "JSSSSSSSS");
                                xueyag_float_y2 = Float.parseFloat(li.get(0));
                                xueyad_float_y2 = Float.parseFloat(li.get(1));
   
                                
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyag_float_y2)
                                                - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyad_float_y2)
                                                - bitmap_point.getHeight() / 2, null);                  
                                c.drawText(li.get(0), interval_left_right * i + interval_left, getHeight()
                                    - buttom - rate1 * xueyag_float_y2, paintxy_text);
                                c.drawText(li.get(1), interval_left_right * i + interval_left, getHeight()
                                        - buttom - rate1 * xueyad_float_y2, paintxy_text);
                              
                                  xueyadaoPath.lineTo(interval_left_right * i + interval_left,
                                            getHeight() - buttom - rate1 * xueyag_float_y2);// 高压下一个点连接线
                                    
                                    xueyadiPath.lineTo(interval_left_right * i + interval_left,
                                            getHeight() - buttom - rate1 * xueyad_float_y2);// 低压下一个点连接线
                                    
                                   // Log.e("4444", "JTTTTTTTTTTTTTTTTTTT");
                            }
                            
                            
                           /* c.drawPath(xueyadaoPath, blPaint);
                            c.drawPath(xueyadiPath, blPaint);*/
                            /*血压高到低画线
                             * 
                             * */
                            
                            c.drawLine(interval_left_right * i + interval_left, getHeight()
                                    - buttom - rate1 * xueyag_float_y2, interval_left_right * i
                                    + interval_left,
                                    getHeight() - buttom - rate1 * xueyad_float_y2, keduPaint);
                         
                        }
                       
                    }
                      
                }
                // ---------------------------------------------------------血压结束-----------------------------------------------
                break;
            case 1:
                
                long daydayTime = 0;
                long tarTime = 0;
                listxt.clear();
                String Monthday[] = new String[7];
                for (int i = 0; i < 7; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 5 * i - 30);
                    Monthday[i] = format.format(cl.getTime());
                  
                }
                c.drawText(Monthday[0], 0, getHeight() - buttom / 4, paint_percent);

                for (int i = 1; i <= 6; i++)
                {
                    c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                            * i + interval_left, getHeight()
                            - buttom, shuxianPaint);
                    c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom - 5,
                            interval_left_right * i + interval_left, getHeight() - buttom,
                            keduPaint);
                    c.drawText(Monthday[i], interval_left_right * i - paint_percent_size,
                            getHeight() - buttom / 4,
                            paint_percent); // 画日期
                }

                // 画具体值
                pathrealdata = new Path();
                long Monthdayint[] = new long[7];

                for (int i = 0; i < 7; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 5 * i - 30);
                    Monthdayint[i] = Long.parseLong(format1.format(cl.getTime()).replace("-", ""));
                }

                iter = milliliter.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
 
                    long time = Long.parseLong(entry.getKey().toString()
                            .substring(0, entry.getKey().toString().length() - 2));
                  /*  
                    System.out.print(time+"\r\n");
                    System.out.print(interval_left_right+"\r\n");
                    System.out.print(interval_left+"\r\n");
                    System.out.print(getWidth()+"\r\n");
                    System.out.print((time / 100) % 100 * interval_left_right+"\r\n");
                    System.out.print((time / 100) % 100 +"\r\n");
                    System.out.print((time / 100) +"\r\n");*/
                    //time = 20160830731;
                    for (int i = 0; i < Monthdayint.length - 1; i++)
                    {
                        if (time <= Monthdayint[i + 1] && time >= Monthdayint[i])
                        {
                            if (x1 == 0 && y1 == 0)
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.moveTo(x1, y1);
                            } else
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.lineTo(x1, y1);
                            }
                            c.drawBitmap(bitmap_point,
                                    x1 - bitmap_point.getWidth() / 2,
                                    y1 - bitmap_point.getHeight() / 2, null);
                            break;
                        }
                    }
                }

                // c.drawPath(pathrealdata, paintdata);

                // --------------------------------------------------------------------血压开始--------------------------------------------------------------
                if (flagtag == 9) 

                {
                   //---------------------------------------------每天画图开始---------------------------
                    /*  每天画图先算出有效值和最早时间差，用单位值的倍数值就是x轴
                     * 
                     * 
                    */
                    String dayday_xueya[] = new String[31];
                    for (int j = 0; j < 31; j++) 
                    {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, j - 30);
                        if (j==0) {
                            tarTime = cl.getTimeInMillis();
                            Log.e("ccccc", "ccccc");
                            System.out.print(tarTime);
                            Log.e("dddd", "ddddddddd");
                        }
                        daydayTime = cl.getTimeInMillis();
                        dayday_xueya[j] = format2.format(cl.getTime());
                        
                        listxt = uService.Findblood(dayday_xueya,
                                MainActivity.NAME, j);
                        if (listxt.size() > 0) {
                            xueyag_float_y1 = Float.parseFloat(listxt.get(0));
                            xueyad_float_y1 = Float.parseFloat(listxt.get(1));
                            long betweendays = (daydayTime - tarTime) / (1000*3600*24);
                            Log.e("yy", "yy");
                            System.out.print(betweendays);
                            Log.e("uu", "uu");
                            xaaa1 =  ((interval_left_right * 6) / 30) * betweendays  +interval_left;
                            System.out.print(xaaa1);
                            c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                    (getHeight()- buttom - rate1 * xueyag_float_y1)
                                    - bitmap_point.getHeight() / 2, null);
                            c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                    (getHeight()- buttom - rate1 * xueyad_float_y1)
                                    - bitmap_point.getHeight() / 2, null);
                        }
                        c.drawLine(xaaa1, getHeight()- buttom - rate1 * xueyag_float_y1, 
                                xaaa1, getHeight()- buttom - rate1 * xueyag_float_y1, keduPaint);

                    }
                    //--------------------------------------------每天画图结束--------------------------------------
                    /*
                     * 每隔5天画图
                     * 
                    */
                    String Monthday_xueya[] = new String[7];
                    for (int i = 0; i < 7; i++)
                    {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, 5 * i - 30);
                       
                        Monthday_xueya[i] = format2.format(cl.getTime());

                        final List<String> li = uService.Findblood(Monthday_xueya,
                                MainActivity.NAME, i);
                        if (li.size() > 0) 
                        {
                           
                            if (Monthday_xueya[i].equals(li.get(2).substring(0, 8))) 
                            {
                               
                                xueyag_float_y2 = Float.parseFloat(li.get(0));
                                xueyad_float_y2 = Float.parseFloat(li.get(1));
                                
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyag_float_y2)
                                                - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyad_float_y2)
                                                - bitmap_point.getHeight() / 2, null);

                                
                            }
                            c.drawLine(interval_left_right * i + interval_left, getHeight()
                                    - buttom - rate1 * xueyag_float_y2, interval_left_right * i
                                    + interval_left,
                                    getHeight() - buttom - rate1 * xueyad_float_y2, keduPaint);
                            
                        }
                        
                    }
                
                }
                // ----------------------------------------------------------------血压结束-------------------------------------------

                break;
            case 2:
                long jiduTime = 0;
                long tarjiduTime = 0;
                listxt.clear();
                
                String jiduday[] = new String[10];
                for (int i = 0; i < 10; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 10 * i - 90);
                    jiduday[i] = format.format(cl.getTime());
                }
                c.drawText(jiduday[0], 0, getHeight() - buttom / 4, paint_percent);

                for (int i = 1; i <= 9; i++)
                {
                    c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                            * i + interval_left, getHeight()
                            - buttom, shuxianPaint);
                    c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom - 5,
                            interval_left_right * i + interval_left, getHeight() - buttom,
                            keduPaint);// 画x轴上面的刻度
                    c.drawText(jiduday[i], interval_left_right * i - paint_percent_size,
                            getHeight() - buttom / 4, paint_percent); // 画日期
                }

                // 画具体值
                pathrealdata = new Path();
                long jidudayint[] = new long[10];

                for (int i = 0; i < 10; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 10 * i - 90);
                    jidudayint[i] = Long.parseLong(format1.format(cl.getTime()).replace("-", ""));
                }

                iter = milliliter.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();

                    long time = Long.parseLong(entry.getKey().toString()
                            .substring(0, entry.getKey().toString().length() - 2));

                    for (int i = 0; i < jidudayint.length - 1; i++)
                    {
                        if (time <= jidudayint[i + 1] && time >= jidudayint[i])
                        {
                            if (x1 == 0 && y1 == 0)
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.moveTo(x1, y1);
                            } else
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.lineTo(x1, y1);
                            }
                            c.drawBitmap(bitmap_point,
                                    x1 - bitmap_point.getWidth() / 2,
                                    y1 - bitmap_point.getHeight() / 2, null);
                            break;
                        }
                    }
                }

                // c.drawPath(pathrealdata, paintdata);

                // ----------------------------------------------------------------------血压开始----------------------------------
                if (flagtag == 9) {
                   
                    String Jidu_xueya[] = new String[91];
                    for (int j = 0; j < 91; j++) 
                    {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, j - 90);
                        if (j==0) {
                            tarjiduTime = cl.getTimeInMillis();
                        }
                        jiduTime = cl.getTimeInMillis();
                        Jidu_xueya[j] = format2.format(cl.getTime());
                        
                        listxt = uService.Findblood(Jidu_xueya,
                                MainActivity.NAME, j);
                        
                        if (listxt.size() > 0) {
                            xueyag_float_y1 = Float.parseFloat(listxt.get(0));
                            xueyad_float_y1 = Float.parseFloat(listxt.get(1));
                            long betweendays = (jiduTime - tarjiduTime) / (1000*3600*24);
                            Log.e("yy", "yy");
                            System.out.print(betweendays);
                            Log.e("uu", "uu");
                            xaaa1 =  ((interval_left_right * 9) / 90) * betweendays  +interval_left;
                            System.out.print(xaaa1);
                            c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                    (getHeight()- buttom - rate1 * xueyag_float_y1)
                                    - bitmap_point.getHeight() / 2, null);
                            c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                    (getHeight()- buttom - rate1 * xueyad_float_y1)
                                    - bitmap_point.getHeight() / 2, null);
                     }
                     c.drawLine(xaaa1, getHeight()- buttom - rate1 * xueyag_float_y1, xaaa1, 
                             getHeight()- buttom - rate1 * xueyad_float_y1, keduPaint);
                     }
                    
                    
                    String Jiduday_xueya[] = new String[10];
                    for (int i = 0; i < 10; i++) {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH,  10*i - 90);
                       
                        Jiduday_xueya[i] = format2.format(cl.getTime());

                        final List<String> li = uService.Findblood(Jiduday_xueya,
                                MainActivity.NAME, i);

                        if (li.size() > 0) 
                        {
                            
                          
                            if (Jiduday_xueya[i].equals(li.get(2).substring(0, 8))) {
                                Log.e("111", "JSSSSSSSS");
                                xueyag_float_y2 = Float.parseFloat(li.get(0));
                                xueyad_float_y2 = Float.parseFloat(li.get(1));

                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyag_float_y2)
                                                - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyad_float_y2)
                                                - bitmap_point.getHeight() / 2, null);

                                xueyadaoPath.lineTo(interval_left_right * i + interval_left,
                                        getHeight() - buttom - rate1 * xueyag_float_y2);// 高压下一个点连接线
                                xueyadiPath.lineTo(interval_left_right * i + interval_left,
                                        getHeight() - buttom - rate1 * xueyad_float_y2);// 低压下一个点连接线
                                Log.e("4444", "JTTTTTTTTTTTTTTTTTTT");
                            }

                            /*
                             * c.drawPath(xueyadaoPath, keduPaint);
                             * c.drawPath(xueyadiPath, keduPaint);
                             */

                            c.drawLine(interval_left_right * i + interval_left, getHeight()
                                    - buttom - rate1 * xueyag_float_y2, interval_left_right * i
                                    + interval_left,
                                    getHeight() - buttom - rate1 * xueyad_float_y2, keduPaint);

                        }
                    }

                }
                // ----------------------------------------------------------------------血压结束----------------------------------

                break;
            case 3:
                long dayYearTime = 0;
                long tarYearTime = 0;
                listxt.clear();
                
                String yearday[] = new String[13];
                for (int i = 0; i < 13; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 30 * i - 360);
                    yearday[i] = format.format(cl.getTime());
                }
                c.drawText(yearday[0], 0, getHeight() - buttom / 4, paint_percent);

                for (int i = 1; i <= 12; i++)
                {
                    c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                            * i + interval_left, getHeight()
                            - buttom, shuxianPaint);
                    c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom - 5,
                            interval_left_right * i + interval_left, getHeight() - buttom,
                            keduPaint);// 画x轴上面的刻度
                    c.drawText(yearday[i], interval_left_right * i - paint_percent_size,
                            getHeight() - buttom / 4, paint_percent); // 画日期
                }

                // 画具体值
                pathrealdata = new Path();
                long yeardayint[] = new long[13];

                for (int i = 0; i < 13; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, 30 * i - 360);
                    yeardayint[i] = Long.parseLong(format1.format(cl.getTime()).replace("-", ""));
                }

                iter = milliliter.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();

                    long time = Long.parseLong(entry.getKey().toString()
                            .substring(0, entry.getKey().toString().length() - 2));

                    for (int i = 0; i < yeardayint.length - 1; i++)
                    {
                        if (time <= yeardayint[i + 1] && time >= yeardayint[i])
                        {
                            if (x1 == 0 && y1 == 0)
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.moveTo(x1, y1);
                            } else
                            {
                                x1 = ((time / 100) % 100 * interval_left_right) / 24
                                        + interval_left_right * i + interval_left;
                                y1 = getHeight() - buttom - rate
                                        * Integer.parseInt(entry.getValue().toString());

                                pathrealdata.lineTo(x1, y1);
                            }
                            c.drawBitmap(bitmap_point,
                                    x1 - bitmap_point.getWidth() / 2,
                                    y1 - bitmap_point.getHeight() / 2, null);
                            break;
                        }
                    }
                }

                // c.drawPath(pathrealdata, paintdata);
                // ---------------------------------------------------血压开始------------------------------------------------------
                if (flagtag == 9) 
                {
                  
                    String Yearday_xueyaday[] = new String[361];
                    for (int j = 0; j < 361; j++) 
                    {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, j - 360);
                        if (j==0) {
                            tarYearTime =cl.getTimeInMillis();
                        }
                        dayYearTime = cl.getTimeInMillis();
                        Yearday_xueyaday[j] = format2.format(cl.getTime());
                        
                        listxt = uService.Findblood(Yearday_xueyaday,
                                MainActivity.NAME, j);
                        
                            if (listxt.size() > 0) {
                                xueyag_float_y1 = Float.parseFloat(listxt.get(0));
                                xueyad_float_y1 = Float.parseFloat(listxt.get(1));
                                long betweendays = (dayYearTime - tarYearTime) / (1000*3600*24);
                                Log.e("yy", "yy");
                                System.out.print(betweendays);
                                Log.e("uu", "uu");
                                //计算天数差betweendays，然后计算总宽度，除以360份，乘以天数就是x轴位置。
                                xaaa1 =  ((interval_left_right * 12) / 360) * betweendays  +interval_left;
                                System.out.print(xaaa1);
                                c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                        (getHeight()- buttom - rate1 * xueyag_float_y1)
                                        - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(bitmap_point, xaaa1 - bitmap_point.getWidth() / 2,
                                        (getHeight()- buttom - rate1 * xueyad_float_y1)
                                        - bitmap_point.getHeight() / 2, null);
                         }
                        c.drawLine(xaaa1, getHeight()- buttom - rate1 * xueyag_float_y1, 
                                xaaa1, getHeight()- buttom - rate1 * xueyad_float_y1, keduPaint);
                        
                    }
                    String Yearday_xueya[] = new String[13];
                    for (int i = 0; i < 13; i++) 
                    {
                        cl.setTime(dateNow);
                        cl.add(Calendar.DAY_OF_MONTH, 30*i - 360);
                        //i为0的时候获取时间差值 
                       
                        Yearday_xueya[i] = format2.format(cl.getTime());

                        final List<String> li = uService.Findblood(Yearday_xueya,
                                MainActivity.NAME, i);
                        if (li.size() > 0) 
                        {
                           
                            if (Yearday_xueya[i - 1].equals(li.get(2).substring(0, 8))) {
                                Log.e("111", "JIKKKKKKKKKKKK");
                                xueyag_float_y1 = Float.parseFloat(li.get(0));
                                xueyad_float_y1 = Float.parseFloat(li.get(1));

                                c.drawBitmap(bitmap_point,
                                        (interval_left_right * (i - 1) + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyag_float_y1)
                                                - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(bitmap_point,
                                        (interval_left_right * (i - 1) + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyad_float_y1)
                                                - bitmap_point.getHeight() / 2, null);
                                Log.e("222", "JEEEEEEEEEEEEEE");

                                xueyadaoPath.moveTo(interval_left_right * (i - 1) + interval_left,
                                        getHeight() - buttom - rate1 * xueyag_float_y1);// 高压开始点
                                xueyadiPath.moveTo(interval_left_right * (i - 1) + interval_left,
                                        getHeight() - buttom - rate1 * xueyad_float_y1);// 低压开始点
                                Log.e("333", "JRRRRRRRRRRRRRRR");
                            }
                            if (Yearday_xueya[i].equals(li.get(2).substring(0, 8))) {
                                Log.e("111", "JSSSSSSSS");
                                xueyag_float_y2 = Float.parseFloat(li.get(0));
                                xueyad_float_y2 = Float.parseFloat(li.get(1));

                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyag_float_y2)
                                                - bitmap_point.getHeight() / 2, null);
                                c.drawBitmap(
                                        bitmap_point,
                                        (interval_left_right * i + interval_left)
                                                - bitmap_point.getWidth() / 2, (getHeight()
                                                - buttom - rate1 * xueyad_float_y2)
                                                - bitmap_point.getHeight() / 2, null);

                                xueyadaoPath.lineTo(interval_left_right * i + interval_left,
                                        getHeight() - buttom - rate1 * xueyag_float_y2);// 高压下一个点连接线
                                xueyadiPath.lineTo(interval_left_right * i + interval_left,
                                        getHeight() - buttom - rate1 * xueyad_float_y2);// 低压下一个点连接线
                                Log.e("4444", "JTTTTTTTTTTTTTTTTTTT");
                            }

                            /*
                             * c.drawPath(xueyadaoPath, keduPaint);
                             * c.drawPath(xueyadiPath, keduPaint);
                             */

                            c.drawLine(interval_left_right * i + interval_left, getHeight()
                                    - buttom - rate1 * xueyag_float_y2, interval_left_right * i
                                    + interval_left,
                                    getHeight() - buttom - rate1 * xueyad_float_y2, keduPaint);

                        }

                    }

                }
                // ---------------------------------------------------血压结束------------------------------------------------------

                break;
            default:
                break;

        }

    }

    // 渐变色
    public void drawBrokenLine(Canvas c) {

        // 渐蓝-渐黄-渐红
        mShader = new LinearGradient(0, 0, 0, getHeight(), new int[] {
                Color.argb(100, 244, 158, 10), Color.argb(45, 244, 120, 10),
                Color.argb(10, 244, 32, 10), Color.argb(100, 215, 217, 20),
                Color.argb(45, 234, 237, 24),
                Color.argb(10, 248, 251, 28), Color.argb(10, 0, 255, 255),
                Color.argb(45, 0, 255, 255), Color.argb(100, 0, 255, 255)
        }, null, Shader.TileMode.CLAMP);

        framPanint.setShader(mShader);
    }

    /**
     * 1,画病类名称 2,画危险值
     */
    public void drawText(Canvas c) {

        float interval = (getHeight() - buttom - top) / 10;
        String[] DisType = {
                "心率", "房性早搏", "室性早搏", "交界性早搏", "心律不齐", "室性心动过速", "房性逸搏", "室性逸博", "心房颤动","血压"
        };
        String[] xinlvtimes = {
                "60次", "90次"
        };
        String fenjie_line[] = {
                "40%", "70%", "100%"
        };
        String fengjie_linexueya[] ={
                "80mmHg","140mmHg"
        };
        switch (flagtag) {
            case 0:
                c.drawText(DisType[0], 15*interval_left, 80, paint_text);
                c.drawText(xinlvtimes[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(xinlvtimes[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                break;
            case 1:
                c.drawText(DisType[1], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 2:
                c.drawText(DisType[2], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 3:
                c.drawText(DisType[3], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 4:
                c.drawText(DisType[4], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 5:
                c.drawText(DisType[5], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 6:
                c.drawText(DisType[6], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 7:
                c.drawText(DisType[7], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 8:
                c.drawText(DisType[8], 15*interval_left, 80, paint_text);
                c.drawText(fenjie_line[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                c.drawText(fenjie_line[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);

                break;
            case 9:
                c.drawText(DisType[9], 15*interval_left, 80, paint_text);
                c.drawText(fengjie_linexueya[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 7+paint_percent_size/2, paint_percent);
                break;
            default:
                break;
        }

    }
}
