
package com.HWDTEMPT.view;

import android.R.color;
import android.R.string;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 */
@SuppressLint("SimpleDateFormat")
public class wenduhomediagram extends View {

    
    private HashMap<Long, String> milliliter;
  /*  private long daydayTime;
    private long tarTime;*/
    private int flagtag;
    private int tabtag;
 //   private float wenduzhi;
    private float interval_left_right;
    private int interval_left=20;//左边的空白
    private int interval_kedu=20;//刻度长度
    private int interval_sum=0;//间隔个数
    private int buttom = 60, top = 70;//底部和顶部的留白
    private Paint paint_percent, paint_text, paintxy_text,paintxy_textJI,paintXYaxis, keduPaint, framPanint,paintdata,huaxian_text,paint_percentjidu,
            shuxianPaint,blPaint,jichutiwen,pailuanqi,pailuanqi1,pailuanqian,pailuanqian1,nvxingtiwen;
    private Shader mShader;
    private Bitmap bitmap_point;
    private int paint_percent_size=40;
    private int paint_percent_sizeji=20;
    private  List<String> listxt = new ArrayList<String>();
    public static boolean youshuju=false;
    public static boolean youshuju1=false;
    public static boolean youshuju2=false;
    public static boolean youshuju3=false;
    public static boolean youshuju4=false;
    public static boolean youshuju5=false;
    public static boolean youshuju6=false;
    public static boolean youshuju7=false;
    public static boolean youshuju105=false;
    private SQLiteHelp uService = new SQLiteHelp(wenduhomediagram.this.getContext());//数据库
    public wenduhomediagram(Context context, HashMap<Long, String> milliliter, int flagtag, int tabtag) {

        super(context);
        init(milliliter, tabtag);
      //  this.wenduzhi=wenduzhi;
        this.flagtag = flagtag;
        this.tabtag = tabtag;

    }

    public void init(HashMap<Long, String> milliliter, int tabtag) {

        this.milliliter = milliliter;
    
        // 数据画笔
        paint_percent = new Paint();
        paint_percent.setStrokeWidth(2);
        paint_percent.setTextSize(paint_percent_size);
        paint_percent.setColor(Color.BLACK);

        jichutiwen = new Paint();
        jichutiwen.setStrokeWidth(2);
        jichutiwen.setTextSize(60);
        jichutiwen.setColor(Color.BLACK);
        
        pailuanqi = new Paint();
        pailuanqi.setStrokeWidth(1);
        pailuanqi.setTextSize(50);
        pailuanqi.setColor(0xffEE82EE);
        
        pailuanqi1 = new Paint();
        pailuanqi1.setStrokeWidth(5);
        pailuanqi1.setTextSize(40);
        pailuanqi1.setColor(0xff228B22);
      
        pailuanqian = new Paint();
        pailuanqian.setStrokeWidth(2);
        pailuanqian.setTextSize(40);
        pailuanqian.setColor(0xff1E90FF);   
        
        pailuanqian1 = new Paint();
        pailuanqian1.setStrokeWidth(5);
        pailuanqian1.setTextSize(40);
        pailuanqian1.setColor(0xff1E90FF);  
        
        nvxingtiwen = new Paint();
        nvxingtiwen.setStrokeWidth(5);
        nvxingtiwen.setTextSize(45);
        nvxingtiwen.setColor(0xff228B22); 
        
        paint_percentjidu = new Paint();
        paint_percentjidu.setStrokeWidth(2);
        paint_percentjidu.setTextSize(30);
        paint_percentjidu.setColor(Color.BLACK);
        
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
       
        paintxy_textJI = new Paint();
        paintxy_textJI.setStrokeWidth(5);
        paintxy_textJI.setTextSize(30);
        paintxy_textJI.setColor(Color.BLACK); 
        
        huaxian_text = new Paint();
        huaxian_text.setStyle(Paint.Style.STROKE);
        huaxian_text.setColor(0XFF00BFFF);
        huaxian_text.setStrokeWidth(12);
        
        paintdata = new Paint();
        paintdata.setStyle(Paint.Style.STROKE);
        paintdata.setColor(Color.RED);
        paintdata.setStrokeWidth(3);

        bitmap_point = BitmapFactory.decodeResource(getResources(),
                R.drawable.page_indicator_focused);
        switch (tabtag)
        {
            case 0:
                interval_sum=7;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left*4);
                SimpleDateFormat format = new SimpleDateFormat("MM-dd");
                SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                Date dateNow = new Date();
                String Weekdayxueya[] = new String[1];
                Calendar cl = Calendar.getInstance();   
                cl.setTime(dateNow);                 
                cl.add(Calendar.DAY_OF_MONTH, 0);
                Weekdayxueya[0] = format2.format(cl.getTime()); 
                final List<String> li = uService.Findwendu2(Weekdayxueya[0],MainActivity.NAME);  
           //     Log.e("ggggggggggg", li.size()+"");
             //   System.out.print("sssssssssssssssssssss"+li.size()+"\n");
             //   int ss=li.size();     
                setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth+interval_left*li.size()*8,LayoutParams.MATCH_PARENT));
            //    Log.e("changduuuuuuuuuuuuu", StaticValue.surfaceviewWidth+interval_left*li.size()*8+"");
                break;
            case 1:
                interval_sum=7;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left*4) /7;
                setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth,LayoutParams.MATCH_PARENT));
          //      Log.e("changduaaaaa", interval_left_right+"");
                break;
            case 2:
                interval_sum=7*4+1;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left*4) /7;
                setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth*4,LayoutParams.MATCH_PARENT));
              //  Log.e("changduaaaaa", interval_left_right+"");
                break;
            case 3:
                interval_sum=89*2;
                interval_left_right = (StaticValue.surfaceviewWidth-interval_left*4*2+3000) /7;
                setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth*4*2+3000,LayoutParams.MATCH_PARENT));
                Log.e("changduaaaaakkk", StaticValue.surfaceviewWidth*4*2+3000+"");
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
        
      
     float interval1 = (getHeight() - buttom - top) / 10; 
        // XY轴
        Path pathXYaxis = new Path();
        pathXYaxis.moveTo(interval_left, top/4);
        pathXYaxis.lineTo(interval_left, getHeight() - buttom);
        pathXYaxis.lineTo(getWidth(), getHeight() - buttom);
        c.drawPath(pathXYaxis, paintXYaxis);
     //   c.drawLine(interval_left, getHeight() - buttom - interval1 * 2, interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, pailuanqian1); 
     //   c.drawLine(interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, interval_left, getHeight() - buttom - interval1 * 4, pailuanqi1); 
        c.drawLine(interval_left, top/4, interval_left-16, top/4+30, paintXYaxis);//X轴箭头。   
        c.drawLine(interval_left, top/4, interval_left+16, top/4+30, paintXYaxis); 
        c.drawLine(getWidth(), getHeight() - buttom, getWidth()-30, getHeight() - buttom-16, paintXYaxis);//Y轴箭头。   
        c.drawLine(getWidth(), getHeight() - buttom, getWidth()-30, getHeight() - buttom+16, paintXYaxis); 

        // 画横线刻度
        float interval = (getHeight() - buttom - top) / 10;
        for (int j = 0; j < 13; j++) {
            if (j ==1 ||j ==5||j ==6||j ==7||j ==9)
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
            if (j == 2)
            {
                // 横向虚线 2
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setStyle(Style.STROKE);
                p.setColor(0xffF0FFFF);
                p.setStrokeWidth((((getHeight() - buttom - top)/50f)*5));
                PathEffect effects = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p.setPathEffect(effects);
                c.drawLine(interval_left+25, getHeight() - buttom - interval * j-(((getHeight() - buttom - top)/50f)*2),  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j-(((getHeight() - buttom - top)/50f)*2), p);
              
                Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
                p1.setStyle(Style.STROKE);
                p1.setColor(Color.GRAY);
                p1.setStrokeWidth(1);
                PathEffect effects1 = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p1.setPathEffect(effects1);   
                c.drawLine(interval_left, getHeight() - buttom - interval * j,  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j, p1);

            }
            if (j == 3)
            {
                // 横向虚线 2
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setStyle(Style.STROKE);
                p.setColor(0xffFFF0F5);
                p.setStrokeWidth(((((getHeight() - buttom - top)/50f)*5)));
                PathEffect effects = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p.setPathEffect(effects);
                c.drawLine(interval_left+25, getHeight() - buttom - interval * j-(((getHeight() - buttom - top)/50f)*3),  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j-(((getHeight() - buttom - top)/50f)*3), p);
               
                Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
                p1.setStyle(Style.STROKE);
                p1.setColor(Color.GRAY);
                p1.setStrokeWidth(1);
                PathEffect effects1 = new DashPathEffect(new float[] {
                        3, 3, 3, 3
                }, 1);
                p1.setPathEffect(effects1);   
                c.drawLine(interval_left, getHeight() - buttom - interval * j,  interval_left_right * interval_sum+interval_left, getHeight() - buttom
                        - interval * j, p1);
            }
            if (j == 4)
            {
                // 横向虚线 2
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
      
            if (j == 8)
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
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date dateNow = new Date();
        Calendar cl = Calendar.getInstance();
       
        Iterator<Entry<Long, String>> iter = milliliter.entrySet().iterator();
        
        Path pathrealdata=new Path();
        int rate=(getHeight() - buttom - top)/100;
        float x1=0,y1=0;
        
        //--------------------------------------血压有关开始------------------
        float xueyag_float_y1 = 0f;
        float xueyag_float_y2 = 0f;
        float xzhouqs=0f;
        float yzhouqs=0f;
        float xzhoujs=0f;
        float yzhoujs=0f;
        
        float xueyad_float_y1 = 0f;
        float xueyad_float_y2 = 0f;
        float xaaa1 = 0f;
        Path xueyadaoPath = new Path();//高压路径
        Path xueyadiPath = new Path();//低压路径
        
        float rate1=(getHeight() - buttom - top)/50f;
       
        
        //--------------------------------------血压有关结束--------------------
        
        switch (tabtag)
        {
        case 3:
            
            String JIday[] = new String[90];
            for (int i = 0; i < 90; i++)
            {
                cl.setTime(dateNow);
                cl.add(Calendar.DAY_OF_MONTH, i - 89);
                JIday[i] = format3.format(cl.getTime());

            }
    /*        c.drawText(JIday[0], interval_left, getHeight() - buttom / 4, paint_percentjidu);
            for (int i = 1; i <= 89; i++)
            {
                c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                        * i + interval_left, getHeight()
                        - buttom, shuxianPaint);
                c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom
                        - interval_kedu,
                        interval_left_right * i + interval_left, getHeight() - buttom,
                        keduPaint);// 画x轴上面的刻度
                c.drawText(JIday[i], interval_left_right * (i+1) - paint_percent_size-interval_left,
                        getHeight() - buttom / 4, paint_percentjidu); // 画日期

            }*/
      //      c.drawLine(interval_left_right * 89 + interval_left, getHeight() - buttom - interval1 * 2, interval_left_right * 89 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, pailuanqian1); 
       //     c.drawLine(interval_left_right * 89 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, interval_left_right * 89 + interval_left, getHeight() - buttom - interval1 * 4, pailuanqi1); 
            // -----------------------------------------------------------血压开始--------------------------------------------------

            if (flagtag == 9)
            {    
            	List<String> shijian = new ArrayList<String>();
                   List<Float> shuju = new ArrayList<Float>();
            	  String JIdayxueya[] = new String[90];
            	  float[] wendushuju = new float[90];
                  for (int i = 0; i < 90; i++) {
                      
                      cl.setTime(dateNow);
                      cl.add(Calendar.DAY_OF_MONTH, i - 89);
                      JIdayxueya[i] = format2.format(cl.getTime());
                      //System.out.print("mmmmmmm"+Weekdayxueya[i]);
                      final List<String> li = uService.Findblood(JIdayxueya, MainActivity.NAME, i);
                      final List<String> liwendu = uService.Findwendu2(JIdayxueya[i], MainActivity.NAME);
                 //     float wendushuju = 0;     
                      String shijianshuju = null;
                      if (li.size() > 0&&liwendu.size()>0) 
                      {
                    	 
                    	  if (JIdayxueya[i].equals(li.get(1).substring(0, 8)))
                          { 
                    
							for (int j = 0; j < liwendu.size(); j++) {
							//	wendushuju[i]= Float.parseFloat(liwendu.get(j).substring(0, 4));
								wendushuju[i]=	Float.parseFloat( Collections.max(liwendu).substring(0, 4));
							}          
                          shijianshuju=JIdayxueya[i].substring(4, 6)+"-"+JIdayxueya[i].substring(6, 8);
                          }
                    	  shijian.add(shijianshuju);
                    	  shuju.add(wendushuju[i]);
                    	
                    	  Log.e("HHHHHHHHHHH", wendushuju[i]+"");  
                    	  Log.e("SSSSSSSSSSSSS", shijianshuju);   
                   // 	  Log.e("sssssssssss", shuju.get(0)+""); 
                    //	  Log.e("sssssssssssrrr", shuju.get(1)+""); 
                    	 /* if (JIdayxueya[i].equals(li.get(1).substring(0, 8)))
                          { 
                              Log.e("111", "JSSSSSSSS");
                              xueyag_float_y2 = Float.parseFloat( Collections.max(li).substring(0, 4));
                     //         xueyad_float_y2 = Float.parseFloat(li.get(1));
                              
                            
                              c.drawBitmap( bitmap_point,(interval_left_right * i + interval_left)- bitmap_point.getWidth() / 2, (getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2, null);                      
                              c.drawText(Collections.max(li).substring(0, 4), interval_left_right * i + interval_left-30, getHeight()- buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_textJI);
                          }*/    
                     
                      }
                    
                  }	
               
              for (int i = 0; i < shuju.size()-1; i++) {
			
            	  xzhouqs=interval_left*(i)*8;
                  xzhoujs=interval_left*(i+1)*8;
                  yzhouqs=(getHeight()- buttom - rate1 * ((shuju.get(i)-35f))*10)- bitmap_point.getHeight() / 2;
                  yzhoujs=(getHeight()- buttom - rate1 * ((shuju.get(i+1)-35f))*10)- bitmap_point.getHeight() / 2;
                 
                  c.drawBitmap(bitmap_point,interval_left+interval_left*(i)*8, (getHeight()- buttom - rate1 * ((shuju.get(i)-35f))*10)- bitmap_point.getHeight() / 2 , null); 
                 c.drawBitmap(bitmap_point,interval_left+interval_left*(i+1)*8, (getHeight()- buttom - rate1 * ((shuju.get(i+1)-35f))*10)- bitmap_point.getHeight() / 2 , null); 
                  c.drawLine(xzhouqs+interval_left+ bitmap_point.getWidth() / 2,yzhouqs+bitmap_point.getHeight() / 2,xzhoujs+interval_left+ bitmap_point.getWidth() / 2,yzhoujs+bitmap_point.getHeight() / 2,huaxian_text);  
                  c.drawText(shuju.get(i)+"", interval_left+interval_left*(i)*8-20, (getHeight()- buttom - rate1 * ((shuju.get(i)-35f))*10)- bitmap_point.getHeight() / 2 -20, paintxy_text);
                  c.drawText(shuju.get(i+1)+"", interval_left+interval_left*(i+1)*8-20, (getHeight()- buttom - rate1 * ((shuju.get(i+1)-35f))*10)- bitmap_point.getHeight() / 2 -20, paintxy_text);  
            
                  c.drawText(shijian.get(i), interval_left+interval_left*(i)*8-20, (getHeight()- buttom/4 ), paintxy_text);
                  //  c.drawText(lii.get(i).substring(4, 6)+"-"+lii.get(i).substring(6, 8), interval_left+interval_left*(i)*8-30, (getHeight()- buttom-10f ), paintxy_text);
                    c.drawText(shijian.get(i+1), interval_left+interval_left*(i+1)*8-20, (getHeight()- buttom/4 ), paintxy_text);    
				
			}
                  
              //    Log.e("ggggggggggg", shuju.size()+"");
              //    Log.e("ggggggggggg222", shujujieguo.get(0)+"");
            }
        break;
        
        case 2:
                
                String Monthday[] = new String[30];
                for (int i = 0; i < 30; i++)
                {
                    cl.setTime(dateNow);
                    cl.add(Calendar.DAY_OF_MONTH, i - 29);
                    Monthday[i] = format.format(cl.getTime());

                }
                c.drawText(Monthday[0], 0, getHeight() - buttom / 4, paint_percent);
                for (int i = 1; i <= 29; i++)
                {
                    c.drawLine(interval_left_right * i + interval_left, top, interval_left_right
                            * i + interval_left, getHeight()
                            - buttom, shuxianPaint);
                    c.drawLine(interval_left_right * i + interval_left, getHeight() - buttom
                            - interval_kedu,
                            interval_left_right * i + interval_left, getHeight() - buttom,
                            keduPaint);// 画x轴上面的刻度
                    c.drawText(Monthday[i], interval_left_right * i - paint_percent_size,
                            getHeight() - buttom / 4, paint_percent); // 画日期

                }
            //    c.drawLine(interval_left_right * 29 + interval_left, getHeight() - buttom - interval1 * 2, interval_left_right * 29 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, pailuanqian1); 
           //     c.drawLine(interval_left_right * 29 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, interval_left_right * 29 + interval_left, getHeight() - buttom - interval1 * 4, pailuanqi1); 
                
                // -----------------------------------------------------------血压开始--------------------------------------------------

                if (flagtag == 9)
                {
                  //  float ss=0;
                    String Weekdayxueya[] = new String[30];
                    float xx1=0f,xx2=0f,xx3=0f,xx4=0f,xx5=0f,xx6=0f,xx7=0f,xx8=0f,xx9=0f,xx10=0f,xx11=0f,xx12=0f,xx13=0f,xx14=0f,xx15=0f,xx16=0f,
                            xx17=0f,xx18=0f,xx19=0f,xx20=0f,xx21=0f,xx22=0f,xx23=0f,xx24=0f,xx25=0f,xx26=0f,xx27=0f,xx28=0f,xx29=0f,xx30=0f ;
                    float yy1=0f,yy2=0f,yy3=0f,yy4=0f,yy5=0f,yy6=0f,yy7=0f,yy8=0f,yy9=0f,yy10=0f,yy11=0f,yy12=0f,yy13=0f,yy14=0f,yy15=0f,yy16=0f,
                            yy17=0f,yy18=0f,yy19=0f,yy20=0f,yy21=0f,yy22=0f,yy23=0f,yy24=0f,yy25=0f,yy26=0f,yy27=0f,yy28=0f,yy29=0f,yy30=0f  ;          
                        System.out.print(dateNow+"fffffffffffffff");      

                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 29);
                        Weekdayxueya[0] = format2.format(cl.getTime());
                        final List<String> li30 = uService.Findwendu(Weekdayxueya[0], MainActivity.NAME);  
                        if (li30.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li30.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li30).substring(0, 4));
                                xx30=(interval_left_right * 0 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy30=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx30, yy30 , null);       
                                c.drawText(Collections.max(li30).substring(0, 4), interval_left_right * 0 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 28);
                        Weekdayxueya[1] = format2.format(cl.getTime());
                        final List<String> li29 = uService.Findwendu(Weekdayxueya[1], MainActivity.NAME);  
                        if (li29.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li29.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li29).substring(0, 4));
                                xx29=(interval_left_right * 1 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy29=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx29, yy29 , null);       
                                c.drawText(Collections.max(li29).substring(0, 4), interval_left_right * 1 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx30!=0f&&yy30!=0f&&xx29!=0f&&yy29!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 27);
                        Weekdayxueya[2] = format2.format(cl.getTime());
                        final List<String> li28 = uService.Findwendu(Weekdayxueya[2], MainActivity.NAME);  
                        if (li28.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li28.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li28).substring(0, 4));
                                xx28=(interval_left_right * 2 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy28=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx28, yy28 , null);       
                                c.drawText(Collections.max(li28).substring(0, 4), interval_left_right * 2 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx29!=0f&&yy29!=0f&&xx28!=0f&&yy28!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 26);
                        Weekdayxueya[3] = format2.format(cl.getTime());
                        final List<String> li27 = uService.Findwendu(Weekdayxueya[3], MainActivity.NAME);  
                        if (li27.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li27.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li27).substring(0, 4));
                                xx27=(interval_left_right * 3 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy27=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx27, yy27 , null);       
                                c.drawText(Collections.max(li27).substring(0, 4), interval_left_right * 3 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx28!=0f&&yy28!=0f&&xx27!=0f&&yy27!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28==0f&&yy27!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 25);
                        Weekdayxueya[4] = format2.format(cl.getTime());
                        final List<String> li26 = uService.Findwendu(Weekdayxueya[4], MainActivity.NAME);  
                        if (li26.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li26.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li26).substring(0, 4));
                                xx26=(interval_left_right * 4 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy26=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx26, yy26 , null);       
                                c.drawText(Collections.max(li26).substring(0, 4), interval_left_right * 4 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx27!=0f&&yy27!=0f&&xx26!=0f&&yy26!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27==0f&&yy26!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28==0f&&yy27==0f&&yy26!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                           
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 24);
                        Weekdayxueya[5] = format2.format(cl.getTime());
                        final List<String> li25 = uService.Findwendu(Weekdayxueya[5], MainActivity.NAME);  
                        if (li25.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li25.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li25).substring(0, 4));
                                xx25=(interval_left_right * 5 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy25=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx25, yy25 , null);       
                                c.drawText(Collections.max(li25).substring(0, 4), interval_left_right * 5 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx26!=0f&&yy26!=0f&&xx25!=0f&&yy25!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26==0f&&yy25!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27==0f&&yy26==0f&&yy25!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28==0f&&yy27==0f&&yy26==0f&&yy25!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                            
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 23);
                        Weekdayxueya[6] = format2.format(cl.getTime());
                        final List<String> li24 = uService.Findwendu(Weekdayxueya[6], MainActivity.NAME);  
                        if (li24.size() > 0) 
                        {
                            if (Weekdayxueya[6].equals(li24.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li24).substring(0, 4));
                                xx24=(interval_left_right * 6 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy24=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx24, yy24 , null);       
                                c.drawText(Collections.max(li24).substring(0, 4), interval_left_right * 6 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }  
                            if (xx25!=0f&&yy25!=0f&&xx24!=0f&&yy24!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25==0f&&yy24!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26==0f&&yy25==0f&&yy24!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27==0f&&yy26==0f&&yy25==0f&&yy24!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28==0f&&yy27==0f&&yy26==0f&&yy25==0f&&yy24!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 22);
                        Weekdayxueya[7] = format2.format(cl.getTime());
                        final List<String> li23 = uService.Findwendu(Weekdayxueya[7], MainActivity.NAME);  
                        if (li23.size() > 0) 
                        {
                            if (Weekdayxueya[8].equals(li23.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li23).substring(0, 4));
                                xx23=(interval_left_right * 7 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy23=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx23, yy23 , null);       
                                c.drawText(Collections.max(li23).substring(0, 4), interval_left_right * 7 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx24!=0f&&yy24!=0f&&xx23!=0f&&yy23!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24==0f&&yy23!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25==0f&&yy24==0f&&yy23!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy29==0f&&yy28==0f&&yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23!=0f&&yy30!=0f) {
                                c.drawLine(xx30+ bitmap_point.getWidth() / 2,yy30+ bitmap_point.getWidth() / 2,xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                                       
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 21);
                        Weekdayxueya[8] = format2.format(cl.getTime());
                        final List<String> li22 = uService.Findwendu(Weekdayxueya[8], MainActivity.NAME);  
                        if (li22.size() > 0) 
                        {
                            if (Weekdayxueya[8].equals(li22.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li22).substring(0, 4));
                                xx22=(interval_left_right * 8 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy22=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx22, yy22 , null);       
                                c.drawText(Collections.max(li22).substring(0, 4), interval_left_right * 8 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx23!=0f&&yy23!=0f&&xx22!=0f&&yy22!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23==0f&&yy22!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24==0f&&yy23==0f&&yy22!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy28==0f&&yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22!=0f&&yy29!=0f) {
                                c.drawLine(xx29+ bitmap_point.getWidth() / 2,yy29+ bitmap_point.getWidth() / 2,xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }      
                        }  
                                    
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 20);
                        Weekdayxueya[9] = format2.format(cl.getTime());
                        final List<String> li21 = uService.Findwendu(Weekdayxueya[9], MainActivity.NAME);  
                        if (li21.size() > 0) 
                        {
                            if (Weekdayxueya[9].equals(li21.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li21).substring(0, 4));
                                xx21=(interval_left_right * 9 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy21=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx21, yy21 , null);       
                                c.drawText(Collections.max(li21).substring(0, 4), interval_left_right * 9 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }  
                            if (xx22!=0f&&yy22!=0f&&xx21!=0f&&yy21!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22==0f&&yy21!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23==0f&&yy22==0f&&yy21!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy27==0f&&yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21!=0f&&yy28!=0f) {
                                c.drawLine(xx28+ bitmap_point.getWidth() / 2,yy28+ bitmap_point.getWidth() / 2,xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                                              
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 19);
                        Weekdayxueya[10] = format2.format(cl.getTime());
                        final List<String> li20 = uService.Findwendu(Weekdayxueya[10], MainActivity.NAME);  
                        if (li20.size() > 0) 
                        {
                            if (Weekdayxueya[16].equals(li20.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li20).substring(0, 4));
                                xx20=(interval_left_right * 10 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy20=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx20, yy20 , null);       
                                c.drawText(Collections.max(li20).substring(0, 4), interval_left_right * 10 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx21!=0f&&yy21!=0f&&xx20!=0f&&yy20!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy21==0f&&yy20!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21==0f&&yy20!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22==0f&&yy21==0f&&yy20!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy26==0f&&yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20!=0f&&yy27!=0f) {
                                c.drawLine(xx27+ bitmap_point.getWidth() / 2,yy27+ bitmap_point.getWidth() / 2,xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }  
                                            
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 18);
                        Weekdayxueya[11] = format2.format(cl.getTime());
                        final List<String> li19 = uService.Findwendu(Weekdayxueya[11], MainActivity.NAME);  
                        if (li19.size() > 0) 
                        {
                            if (Weekdayxueya[16].equals(li19.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li19).substring(0, 4));
                                xx19=(interval_left_right * 11 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy19=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx19, yy19 , null);       
                                c.drawText(Collections.max(li19).substring(0, 4), interval_left_right * 11 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx20!=0f&&yy20!=0f&&xx19!=0f&&yy19!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy20==0f&&yy19!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy21==0f&&yy20==0f&&yy19!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21==0f&&yy20==0f&&yy19!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy25==0f&&yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19!=0f&&yy26!=0f) {
                                c.drawLine(xx26+ bitmap_point.getWidth() / 2,yy26+ bitmap_point.getWidth() / 2,xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,huaxian_text);    
                            } 
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 17);
                        Weekdayxueya[12] = format2.format(cl.getTime());
                        final List<String> li18 = uService.Findwendu(Weekdayxueya[12], MainActivity.NAME);  
                        if (li18.size() > 0) 
                        {
                            if (Weekdayxueya[12].equals(li18.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li18).substring(0, 4));
                                xx18=(interval_left_right * 12 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy18=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx18, yy18 , null);       
                                c.drawText(Collections.max(li18).substring(0, 4), interval_left_right * 12 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx19!=0f&&yy19!=0f&&xx18!=0f&&yy18!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy19==0f&&yy18!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy20==0f&&yy19==0f&&yy18!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy21==0f&&yy20==0f&&yy19==0f&&yy18!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy24==0f&&yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18!=0f&&yy25!=0f) {
                                c.drawLine(xx25+ bitmap_point.getWidth() / 2,yy25+ bitmap_point.getWidth() / 2,xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }   
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 16);
                        Weekdayxueya[13] = format2.format(cl.getTime());
                        final List<String> li17 = uService.Findwendu(Weekdayxueya[13], MainActivity.NAME);  
                        if (li17.size() > 0) 
                        {
                            if (Weekdayxueya[13].equals(li17.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li17).substring(0, 4));
                                xx17=(interval_left_right * 13 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy17=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx17, yy17 , null);       
                                c.drawText(Collections.max(li17).substring(0, 4), interval_left_right * 13 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx18!=0f&&yy18!=0f&&xx17!=0f&&yy17!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy18==0f&&yy17!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy19==0f&&yy18==0f&&yy17!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy20==0f&&yy19==0f&&yy18==0f&&yy17!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy23==0f&&yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17!=0f&&yy24!=0f) {
                                c.drawLine(xx24+ bitmap_point.getWidth() / 2,yy24+ bitmap_point.getWidth() / 2,xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,huaxian_text);    
                            } 
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 15);
                        Weekdayxueya[14] = format2.format(cl.getTime());
                        final List<String> li16 = uService.Findwendu(Weekdayxueya[14], MainActivity.NAME);  
                        if (li16.size() > 0) 
                        {
                            if (Weekdayxueya[14].equals(li16.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li16).substring(0, 4));
                                xx16=(interval_left_right * 14 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy16=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx16, yy16 , null);       
                                c.drawText(Collections.max(li16).substring(0, 4), interval_left_right * 14 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx17!=0f&&yy17!=0f&&xx16!=0f&&yy16!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy17==0f&&yy16!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy18==0f&&yy17==0f&&yy16!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy19==0f&&yy18==0f&&yy17==0f&&yy16!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy20==0f&&yy19==0f&&yy18==0f&&yy17==0f&&yy16!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17==0f&&yy16!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy22==0f&&yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17==0f&&yy16!=0f&&yy23!=0f) {
                                c.drawLine(xx23+ bitmap_point.getWidth() / 2,yy23+ bitmap_point.getWidth() / 2,xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }   
                        }  
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 14);
                        Weekdayxueya[15] = format2.format(cl.getTime());
                        final List<String> li15 = uService.Findwendu(Weekdayxueya[15], MainActivity.NAME);  
                        if (li15.size() > 0) 
                        {
                            if (Weekdayxueya[15].equals(li15.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li15).substring(0, 4));
                                xx15=(interval_left_right * 15 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy15=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx15, yy15 , null);       
                                c.drawText(Collections.max(li15).substring(0, 4), interval_left_right * 15 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx16!=0f&&yy16!=0f&&xx15!=0f&&yy15!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy16==0f&&yy15!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }else if (yy17==0f&&yy16==0f&&yy15!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }else if (yy18==0f&&yy17==0f&&yy16==0f&&yy15!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }else if (yy19==0f&&yy18==0f&&yy17==0f&&yy16==0f&&yy15!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }else if (yy20==0f&&yy19==0f&&yy18==0f&&yy17==0f&&yy16==0f&&yy15!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }else if (yy21==0f&&yy20==0f&&yy19==0f&&yy18==0f&&yy17==0f&&yy16==0f&&yy15!=0f&&yy22!=0f) {
                                c.drawLine(xx22+ bitmap_point.getWidth() / 2,yy22+ bitmap_point.getWidth() / 2,xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,huaxian_text);     
                            }
                        }      
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 13);
                        Weekdayxueya[16] = format2.format(cl.getTime());
                        final List<String> li14 = uService.Findwendu(Weekdayxueya[16], MainActivity.NAME);  
                        if (li14.size() > 0) 
                        {
                            if (Weekdayxueya[16].equals(li14.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li14).substring(0, 4));
                                xx14=(interval_left_right * 16 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy14=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx14, yy14 , null);       
                                c.drawText(Collections.max(li14).substring(0, 4), interval_left_right * 16 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx15!=0f&&yy15!=0f&&xx14!=0f&&yy14!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy15==0f&&yy14!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy16==0f&&yy15==0f&&yy14!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy17==0f&&yy16==0f&&yy15==0f&&yy14!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy18==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy19==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy20==0f&&yy19==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14!=0f&&yy21!=0f) {
                                c.drawLine(xx21+ bitmap_point.getWidth() / 2,yy21+ bitmap_point.getWidth() / 2,xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 12);
                        Weekdayxueya[17] = format2.format(cl.getTime());
                        final List<String> li13 = uService.Findwendu(Weekdayxueya[17], MainActivity.NAME);  
                        if (li13.size() > 0) 
                        {
                            if (Weekdayxueya[17].equals(li13.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li13).substring(0, 4));
                                xx13=(interval_left_right * 17 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy13=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx13, yy13 , null);       
                                c.drawText(Collections.max(li13).substring(0, 4), interval_left_right * 17 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            }
                            if (xx14!=0f&&yy14!=0f&&xx13!=0f&&yy13!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy14==0f&&yy13!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy15==0f&&yy14==0f&&yy13!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy16==0f&&yy15==0f&&yy14==0f&&yy13!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy17==0f&&yy16==0f&&yy15==0f&&yy14==0f&&yy13!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy18==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14==0f&&yy13!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy19==0f&&yy18==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14==0f&&yy13!=0f&&yy20!=0f) {
                                c.drawLine(xx20+ bitmap_point.getWidth() / 2,yy20+ bitmap_point.getWidth() / 2,xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 11);
                        Weekdayxueya[18] = format2.format(cl.getTime());
                        final List<String> li12 = uService.Findwendu(Weekdayxueya[18], MainActivity.NAME);  
                        if (li12.size() > 0) 
                        {
                            if (Weekdayxueya[18].equals(li12.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li12).substring(0, 4));
                                xx12=(interval_left_right * 18 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy12=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx12, yy12 , null);       
                                c.drawText(Collections.max(li12).substring(0, 4), interval_left_right * 18 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx13!=0f&&yy13!=0f&&xx12!=0f&&yy12!=0f) {
                                c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy13==0f&&yy12!=0f&&yy14!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }else if (yy14==0f&&yy13==0f&&yy12!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }else if (yy15==0f&&yy14==0f&&yy13==0f&&yy12!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }else if (yy16==0f&&yy15==0f&&yy14==0f&&yy13==0f&&yy12!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }else if (yy17==0f&&yy16==0f&&yy15==0f&&yy14==0f&&yy13==0f&&yy12!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }else if (yy18==0f&&yy17==0f&&yy16==0f&&yy15==0f&&yy14==0f&&yy13==0f&&yy12!=0f&&yy19!=0f) {
                                c.drawLine(xx19+ bitmap_point.getWidth() / 2,yy19+ bitmap_point.getWidth() / 2,xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,huaxian_text);      
                            }
                        }
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 10);
                        Weekdayxueya[19] = format2.format(cl.getTime());
                        final List<String> li11 = uService.Findwendu(Weekdayxueya[19], MainActivity.NAME);  
                        if (li11.size() > 0) 
                        {
                            if (Weekdayxueya[19].equals(li11.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li11).substring(0, 4));
                                xx11=(interval_left_right * 19 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy11=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx11, yy11 , null);       
                                c.drawText(Collections.max(li11).substring(0, 4), interval_left_right * 19 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx12!=0f&&yy12!=0f&&xx11!=0f&&yy11!=0f) {
                                c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx12==0f&&yy11!=0f&&yy13!=0f) {
                                c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx13==0f&&xx12==0f&&yy11!=0f&&yy14!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx14==0f&&xx13==0f&&xx12==0f&&yy11!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx15==0f&&xx14==0f&&xx13==0f&&xx12==0f&&yy11!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx16==0f&&xx15==0f&&xx14==0f&&xx13==0f&&xx12==0f&&yy11!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (xx17==0f&&xx16==0f&&xx15==0f&&xx14==0f&&xx13==0f&&xx12==0f&&yy11!=0f&&yy18!=0f) {
                                c.drawLine(xx18+ bitmap_point.getWidth() / 2,yy18+ bitmap_point.getWidth() / 2,xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 9);
                        Weekdayxueya[20] = format2.format(cl.getTime());
                        final List<String> li10 = uService.Findwendu(Weekdayxueya[20], MainActivity.NAME);  
                        if (li10.size() > 0) 
                        {
                            if (Weekdayxueya[20].equals(li10.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li10).substring(0, 4));
                                xx10=(interval_left_right * 20 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy10=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx10, yy10 , null);       
                                c.drawText(Collections.max(li10).substring(0, 4), interval_left_right * 20 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx11!=0f&&yy11!=0f&&xx10!=0f&&yy10!=0f) {
                                c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy11==0&&yy10!=0f&&yy12!=0f) {
                                c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy12==0&&yy11==0&&yy10!=0f&&yy13!=0f) {
                                c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy13==0&&yy12==0&&yy11==0&&yy10!=0f&&yy14!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy14==0&&yy13==0&&yy12==0&&yy11==0&&yy10!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy15==0&&yy14==0&&yy13==0&&yy12==0&&yy11==0&&yy10!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy16==0&&yy15==0&&yy14==0&&yy13==0&&yy12==0&&yy11==0&&yy10!=0f&&yy17!=0f) {
                                c.drawLine(xx17+ bitmap_point.getWidth() / 2,yy17+ bitmap_point.getWidth() / 2,xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }
                                           
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 8);
                        Weekdayxueya[21] = format2.format(cl.getTime());
                        final List<String> li09 = uService.Findwendu(Weekdayxueya[21], MainActivity.NAME);  
                        if (li09.size() > 0) 
                        {
                            Log.e("size", li09.size()+"");
                            if (Weekdayxueya[21].equals(li09.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li09).substring(0, 4));
                                xx9=(interval_left_right * 21 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy9=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx1, yy1 , null);       
                                c.drawText(Collections.max(li09).substring(0, 4), interval_left_right * 21 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx10!=0f&&yy10!=0f&&xx9!=0f&&yy9!=0f) {
                                c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy10==0&&yy9!=0f&&yy11!=0f) {
                                c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy11==0&&yy10==0&&yy9!=0f&&yy12!=0f) {
                                c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy12==0&&yy11==0&&yy10==0&&yy9!=0f&&yy13!=0f) {
                                c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy13==0&&yy12==0&&yy11==0&&yy10==0&&yy9!=0f&&yy14!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy14==0&&yy13==0&&yy12==0&&yy11==0&&yy10==0&&yy9!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy15==0&&yy14==0&&yy13==0&&yy12==0&&yy11==0&&yy10==0&&yy9!=0f&&yy16!=0f) {
                                c.drawLine(xx16+ bitmap_point.getWidth() / 2,yy16+ bitmap_point.getWidth() / 2,xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }
                        }
                        
                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 7);
                        Weekdayxueya[22] = format2.format(cl.getTime());
                        final List<String> li = uService.Findwendu(Weekdayxueya[22], MainActivity.NAME);  

                        if (li.size() > 0) 
                        {
                            Log.e("size", li.size()+"");
                            if (Weekdayxueya[22].equals(li.get(1).substring(0, 8)))
                            { 
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li).substring(0, 4));
                                xx1=(interval_left_right * 22 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy1=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx1, yy1 , null);       
                                c.drawText(Collections.max(li).substring(0, 4), interval_left_right * 22 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                            if (xx9!=0f&&yy9!=0f&&xx1!=0f&&yy1!=0f) {
                                c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy9==0&&yy1!=0f&&yy10!=0f) {
                                c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy10==0f&&yy9==0&&yy1!=0f&&yy11!=0f) {
                                c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy11==0f&&yy10==0f&&yy9==0&&yy1!=0f&&yy12!=0f) {
                                c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy12==0f&&yy11==0f&&yy10==0f&&yy9==0&&yy1!=0f&&yy13!=0f) {
                                c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy13==0f&&yy12==0f&&yy11==0f&&yy10==0f&&yy9==0&&yy1!=0f&&yy14!=0f) {
                                c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }else if (yy14==0f&&yy13==0f&&yy12==0f&&yy11==0f&&yy10==0f&&yy9==0&&yy1!=0f&&yy15!=0f) {
                                c.drawLine(xx15+ bitmap_point.getWidth() / 2,yy15+ bitmap_point.getWidth() / 2,xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,huaxian_text);    
                            }      
                            
                        }
 
                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 6);
                                Weekdayxueya[23] = format2.format(cl.getTime());
                                final List<String> li1 = uService.Findwendu(Weekdayxueya[23], MainActivity.NAME);  
                                if (li1.size() > 0) {
                                        if (Weekdayxueya[23].equals(li1.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li1).substring(0, 4));
                                            xx2=(interval_left_right * 23 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy2=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx2, yy2 , null);       
                                            c.drawText(Collections.max(li1).substring(0, 4), interval_left_right * 23 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx1!=0f&&yy1!=0f&&xx2!=0f&&yy2!=0f) {
                                            c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy1==0f&&yy2!=0f&&yy9!=0f) {
                                            c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy9==0f&&yy1==0f&&yy2!=0f&&yy10!=0f) {
                                            c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy10==0f&&yy9==0f&&yy1==0f&&yy2!=0f&&yy11!=0f) {
                                            c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy11==0f&&yy10==0f&&yy9==0f&&yy1==0f&&yy2!=0f&&yy12!=0f) {
                                            c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy12==0f&&yy11==0f&&yy10==0f&&yy9==0f&&yy1==0f&&yy2!=0f&&yy13!=0f) {
                                            c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy13==0f&&yy12==0f&&yy11==0f&&yy10==0f&&yy9==0f&&yy1==0f&&yy2!=0f&&yy14!=0f) {
                                            c.drawLine(xx14+ bitmap_point.getWidth() / 2,yy14+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }        
                                } 
                                
 

                                   cl.setTime(dateNow);   
                                   cl.add(Calendar.DAY_OF_MONTH, - 5);
                                   Weekdayxueya[24] = format2.format(cl.getTime());
                                   final List<String> li2 = uService.Findwendu(Weekdayxueya[24], MainActivity.NAME);  
                                   if (li2.size() > 0) {
                                           if (Weekdayxueya[24].equals(li2.get(1).substring(0, 8)))
                                           { 
                                               xueyag_float_y2 = Float.parseFloat( Collections.max(li2).substring(0, 4));
                                               xx3=(interval_left_right * 24 + interval_left)- bitmap_point.getWidth() / 2;      
                                               yy3=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                               c.drawBitmap(bitmap_point,xx3, yy3 , null);       
                                               c.drawText(Collections.max(li2).substring(0, 4), interval_left_right * 24 + interval_left-30, getHeight()
                                                   - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                           } 
                                           if (xx2!=0f&&yy2!=0f&&xx3!=0f&&yy3!=0f) {
                                               c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text);    
                                           }else if (yy2==0f&&yy3!=0f&&yy1!=0f) {
                                               c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }else if (yy1==0f&&yy2==0f&&yy3!=0f&&yy9!=0f) {
                                               c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }else if (yy9==0f&&yy1==0f&&yy2==0f&&yy3!=0f&&yy10!=0f) {
                                               c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }else if (yy10==0f&&yy9==0f&&yy1==0f&&yy2==0f&&yy3!=0f&&yy11!=0f) {
                                               c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }else if (yy11==0f&&yy10==0f&&yy9==0f&&yy1==0f&&yy2==0f&&yy3!=0f&&yy12!=0f) {
                                               c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }else if (yy12==0f&&yy11==0f&&yy10==0f&&yy9==0f&&yy1==0f&&yy2==0f&&yy3!=0f&&yy13!=0f) {
                                               c.drawLine(xx13+ bitmap_point.getWidth() / 2,yy13+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }          
                                   } 

                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 4);
                                Weekdayxueya[25] = format2.format(cl.getTime());
                                final List<String> li3 = uService.Findwendu(Weekdayxueya[25], MainActivity.NAME);  
                                if (li3.size() > 0) {
                                        if (Weekdayxueya[25].equals(li3.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li3).substring(0, 4));
                                            xx4=(interval_left_right * 25 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy4=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx4, yy4 , null);       
                                            c.drawText(Collections.max(li3).substring(0, 4), interval_left_right * 25 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx3!=0f&&yy3!=0f&&xx4!=0f&&yy4!=0f) {
                                            c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy3==0f&&yy4!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy1==0&&yy2==0f&&yy3==0f&&yy4!=0f&&yy9!=0f) {
                                       c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy9==0&&yy1==0&&yy2==0f&&yy3==0f&&yy4!=0f&&yy10!=0f) {
                                       c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy10==0&&yy9==0&&yy1==0&&yy2==0f&&yy3==0f&&yy4!=0f&&yy11!=0f) {
                                       c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy11==0f&&yy10==0&&yy9==0&&yy1==0&&yy2==0f&&yy3==0f&&yy4!=0f&&yy12!=0f) {
                                       c.drawLine(xx12+ bitmap_point.getWidth() / 2,yy12+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }      
                                } 
                                


                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 3);
                                Weekdayxueya[26] = format2.format(cl.getTime());
                                final List<String> li4 = uService.Findwendu(Weekdayxueya[26], MainActivity.NAME);  
                                if (li4.size() > 0) {
                                        if (Weekdayxueya[26].equals(li4.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li4).substring(0, 4));
                                            xx5=(interval_left_right * 26 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy5=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx5, yy5 , null);       
                                            c.drawText(Collections.max(li4).substring(0, 4), interval_left_right * 26 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx4!=0f&&yy4!=0f&&xx5!=0f&&yy5!=0f) {
                                            c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy4==0f&&yy5!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy1==0f&&yy2==0f&&yy3==0f&&yy4==0f&&yy5!=0f&&yy9!=0f) {
                                       c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy9==0f&&yy1==0f&&yy2==0f&&yy3==0f&&yy4==0f&&yy5!=0f&&yy10!=0f) {
                                       c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy10==0f&&yy9==0f&&yy1==0f&&yy2==0f&&yy3==0f&&yy4==0f&&yy5!=0f&&yy11!=0f) {
                                       c.drawLine(xx11+ bitmap_point.getWidth() / 2,yy11+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }       
                                } 

                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 2);
                                Weekdayxueya[27] = format2.format(cl.getTime());
                                final List<String> li5 = uService.Findwendu(Weekdayxueya[27], MainActivity.NAME);  
                                if (li5.size() > 0) {
                                        if (Weekdayxueya[27].equals(li5.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li5).substring(0, 4));
                                            xx6=(interval_left_right * 27 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy6=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx6, yy6 , null);       
                                            c.drawText(Collections.max(li5).substring(0, 4), interval_left_right * 27 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx5!=0f&&yy5!=0f&&xx6!=0f&&yy6!=0f) {
                                            c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy5==0f&&yy6!=0f&&yy4!=0f) {
                                       c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy4==0f&&yy5==0f&&yy6!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        } else if (yy1==0f&&yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy9!=0f) {
                                       c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        } else if (yy9==0&&yy1==0f&&yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy10!=0f) {
                                       c.drawLine(xx10+ bitmap_point.getWidth() / 2,yy10+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }      
                                } 


                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 1);
                                Weekdayxueya[28] = format2.format(cl.getTime());
                                final List<String> li6 = uService.Findwendu( Weekdayxueya[28], MainActivity.NAME);  
                                if (li6.size() > 0) {
                                        if (Weekdayxueya[28].equals(li6.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li6).substring(0, 4));
                                            xx7=(interval_left_right * 28 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy7=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx7, yy7 , null);       
                                            c.drawText(Collections.max(li6).substring(0, 4), interval_left_right * 28 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx6!=0f&&yy6!=0f&&xx7!=0f&&yy7!=0f) {
                                            c.drawLine(xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy6==0f&&yy7!=0f&&yy5!=0f) {
                                       c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy5==0f&&yy6==0f&&yy7!=0f&&yy4!=0f) {
                                       c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy1==0&&yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy9!=0f) {
                                       c.drawLine(xx9+ bitmap_point.getWidth() / 2,yy9+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }    
                                        
                                } 
                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, 0);
                                Weekdayxueya[29] = format2.format(cl.getTime());
                                final List<String> li7 = uService.Findwendu(Weekdayxueya[29], MainActivity.NAME);  
                                if (li7.size() > 0) {
                                        if (Weekdayxueya[29].equals(li7.get(1).substring(0, 8)))
                                        { 
                                   
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li7).substring(0, 4));
                                            xx8=(interval_left_right * 29 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy8=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx8, yy8 , null);       
                                            c.drawText(Collections.max(li7).substring(0, 4), interval_left_right * 29 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                          if (xx7!=0f&&yy7!=0f&&xx8!=0f&&yy8!=0f) {
                                        c.drawLine(xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy7==0f&&yy8!=0f&&yy6!=0f) {
                                        c.drawLine(xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy6==0f&&yy7==0f&&yy8!=0f&&yy5!=0f) {
                                        c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy4!=0f) {
                                        c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy3!=0f) {
                                        c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy2!=0f) {
                                        c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy1!=0f) {
                                        c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }              
                                } 
                          
                }
                // ---------------------------------------------------------血压结束-----------------------------------------------
                break; 
 
            case 1:
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
           //     c.drawLine(interval_left_right * 7 + interval_left, getHeight() - buttom - interval1 * 2, interval_left_right * 7 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, pailuanqian1); 
           //     c.drawLine(interval_left_right * 7 + interval_left, getHeight() - buttom - interval1 * 3-(getHeight() - buttom - top)/50f, interval_left_right * 7 + interval_left, getHeight() - buttom - interval1 * 4, pailuanqi1); 
                // -----------------------------------------------------------血压开始--------------------------------------------------

                if (flagtag == 9)
                {
                  //  float ss=0;
                    String Weekdayxueya[] = new String[8];
                    float xx1=0f,xx2=0f,xx3=0f,xx4=0f,xx5=0f,xx6=0f,xx7=0f,xx8=0f,x00=0f,x01=0f;
                    float yy1=0f,yy2=0f,yy3=0f,yy4=0f,yy5=0f,yy6=0f,yy7=0f,yy8=0f,y00=0f,y01=0f;          
                        System.out.print(dateNow+"fffffffffffffff");      
                     //  System.out.print("mmmmmmm"+Weekdayxueya[i]+"\n");

                        cl.setTime(dateNow);                 
                        cl.add(Calendar.DAY_OF_MONTH, - 7);
                        Weekdayxueya[0] = format2.format(cl.getTime());
                        final List<String> li = uService.Findwendu(Weekdayxueya[0], MainActivity.NAME);  

                        if (li.size() > 0) 
                        {
                            Log.e("size", li.size()+"");
                          //  Log.e("length",  li.get(0).length()+"");
                            if (Weekdayxueya[0].equals(li.get(1).substring(0, 8)))
                            { 
                               /*int st=i;
                               ss= Float.parseFloat( Collections.max(li).substring(0, 4));
                               List<Float> fl = new ArrayList<Float>();    // 泛型，这ArrayList只能放Float类型
                               fl.add(ss);
                               Float[] f = new Float[fl.size()];
                         //      System.out.println(f[st]);
                               for (int s = st; s < fl.size(); s++) {
                                   f[s] = fl.get(s);    
                                   System.out.println(f.length+"222222222");
                                   System.out.println(f[st]);       
                               }
                                  */ 
                                Log.e("110000000000001", Collections.max(li).substring(0, 4));
                              // Log.e("111", "JSSSSSSSS");
                                xueyag_float_y2 = Float.parseFloat( Collections.max(li).substring(0, 4));
                              // xueyad_float_y2 = Float.parseFloat(li.get(1));
                               Log.e("110000000000000", li.get(0));
                               Log.e("110000000000001", li.get(1));
                                xx1=(interval_left_right * 0 + interval_left)- bitmap_point.getWidth() / 2;      
                                yy1=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                c.drawBitmap(bitmap_point,xx1, yy1 , null);       
                                c.drawText(Collections.max(li).substring(0, 4), interval_left_right * 0 + interval_left-30, getHeight()
                                    - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                            } 
                        }
 
                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 6);
                                Weekdayxueya[1] = format2.format(cl.getTime());
                                final List<String> li1 = uService.Findwendu(Weekdayxueya[1], MainActivity.NAME);  
                                if (li1.size() > 0) {
                                        if (Weekdayxueya[1].equals(li1.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li1).substring(0, 4));
                                            xx2=(interval_left_right * 1 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy2=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx2, yy2 , null);       
                                            c.drawText(Collections.max(li1).substring(0, 4), interval_left_right * 1 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                } 
                                if (xx1!=0f&&yy1!=0f&&xx2!=0f&&yy2!=0f) {
                                    c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,huaxian_text);    
                                } 
 

                                   cl.setTime(dateNow);   
                                   cl.add(Calendar.DAY_OF_MONTH, - 5);
                                   Weekdayxueya[2] = format2.format(cl.getTime());
                                   final List<String> li2 = uService.Findwendu(Weekdayxueya[2], MainActivity.NAME);  
                                   if (li2.size() > 0) {
                                           if (Weekdayxueya[2].equals(li2.get(1).substring(0, 8)))
                                           { 
                                               xueyag_float_y2 = Float.parseFloat( Collections.max(li2).substring(0, 4));
                                               xx3=(interval_left_right * 2 + interval_left)- bitmap_point.getWidth() / 2;      
                                               yy3=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                               c.drawBitmap(bitmap_point,xx3, yy3 , null);       
                                               c.drawText(Collections.max(li2).substring(0, 4), interval_left_right * 2 + interval_left-30, getHeight()
                                                   - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                           } 
                                           if (xx2!=0f&&yy2!=0f&&xx3!=0f&&yy3!=0f) {
                                               c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text);    
                                           }else if (yy2==0f&&yy3!=0f&&yy1!=0f) {
                                               c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,huaxian_text); 
                                           }  
                                   } 

                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 4);
                                Weekdayxueya[3] = format2.format(cl.getTime());
                                final List<String> li3 = uService.Findwendu(Weekdayxueya[3], MainActivity.NAME);  
                                if (li3.size() > 0) {
                                        if (Weekdayxueya[3].equals(li3.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li3).substring(0, 4));
                                            xx4=(interval_left_right * 3 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy4=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx4, yy4 , null);       
                                            c.drawText(Collections.max(li3).substring(0, 4), interval_left_right * 3 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx3!=0f&&yy3!=0f&&xx4!=0f&&yy4!=0f) {
                                            c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy3==0f&&yy4!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }  
                                } 
                                


                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 3);
                                Weekdayxueya[4] = format2.format(cl.getTime());
                                final List<String> li4 = uService.Findwendu(Weekdayxueya[4], MainActivity.NAME);  
                                if (li4.size() > 0) {
                                        if (Weekdayxueya[4].equals(li4.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li4).substring(0, 4));
                                            xx5=(interval_left_right * 4 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy5=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx5, yy5 , null);       
                                            c.drawText(Collections.max(li4).substring(0, 4), interval_left_right * 4 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx4!=0f&&yy4!=0f&&xx5!=0f&&yy5!=0f) {
                                            c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy4==0f&&yy5!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }    
                                } 

                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 2);
                                Weekdayxueya[5] = format2.format(cl.getTime());
                                final List<String> li5 = uService.Findwendu(Weekdayxueya[5], MainActivity.NAME);  
                                if (li5.size() > 0) {
                                        if (Weekdayxueya[5].equals(li5.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li5).substring(0, 4));
                                            xx6=(interval_left_right * 5 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy6=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx6, yy6 , null);       
                                            c.drawText(Collections.max(li5).substring(0, 4), interval_left_right * 5 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx5!=0f&&yy5!=0f&&xx6!=0f&&yy6!=0f) {
                                            c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy5==0f&&yy6!=0f&&yy4!=0f) {
                                       c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy4==0f&&yy5==0f&&yy6!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }   
                                } 


                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, - 1);
                                Weekdayxueya[6] = format2.format(cl.getTime());
                                final List<String> li6 = uService.Findwendu( Weekdayxueya[6], MainActivity.NAME);  
                                if (li6.size() > 0) {
                                        if (Weekdayxueya[6].equals(li6.get(1).substring(0, 8)))
                                        { 
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li6).substring(0, 4));
                                            xx7=(interval_left_right * 6 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy7=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx7, yy7 , null);       
                                            c.drawText(Collections.max(li6).substring(0, 4), interval_left_right * 6 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                        if (xx6!=0f&&yy6!=0f&&xx7!=0f&&yy7!=0f) {
                                            c.drawLine(xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text);    
                                        }else if (yy6==0f&&yy7!=0f&&yy5!=0f) {
                                       c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy5==0f&&yy6==0f&&yy7!=0f&&yy4!=0f) {
                                       c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy3!=0f) {
                                       c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy2!=0f) {
                                       c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7!=0f&&yy1!=0f) {
                                       c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,huaxian_text); 
                                        }  
                                        
                                } 
                                cl.setTime(dateNow);   
                                cl.add(Calendar.DAY_OF_MONTH, 0);
                                Weekdayxueya[7] = format2.format(cl.getTime());
                                final List<String> li7 = uService.Findwendu(Weekdayxueya[7], MainActivity.NAME);  
                                if (li7.size() > 0) {
                                        if (Weekdayxueya[7].equals(li7.get(1).substring(0, 8)))
                                        { 
                                   
                                            xueyag_float_y2 = Float.parseFloat( Collections.max(li7).substring(0, 4));
                                            xx8=(interval_left_right * 7 + interval_left)- bitmap_point.getWidth() / 2;      
                                            yy8=(getHeight()- buttom - rate1 * ((xueyag_float_y2-35f))*10)- bitmap_point.getHeight() / 2;
                                            c.drawBitmap(bitmap_point,xx8, yy8 , null);       
                                            c.drawText(Collections.max(li7).substring(0, 4), interval_left_right * 7 + interval_left-30, getHeight()
                                                - buttom -  rate1 * ((xueyag_float_y2-35f))*10-20, paintxy_text);
                                        } 
                                          if (xx7!=0f&&yy7!=0f&&xx8!=0f&&yy8!=0f) {
                                        c.drawLine(xx7+ bitmap_point.getWidth() / 2,yy7+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy7==0f&&yy8!=0f&&yy6!=0f) {
                                        c.drawLine(xx6+ bitmap_point.getWidth() / 2,yy6+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy6==0f&&yy7==0f&&yy8!=0f&&yy5!=0f) {
                                        c.drawLine(xx5+ bitmap_point.getWidth() / 2,yy5+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy4!=0f) {
                                        c.drawLine(xx4+ bitmap_point.getWidth() / 2,yy4+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy3!=0f) {
                                        c.drawLine(xx3+ bitmap_point.getWidth() / 2,yy3+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy2!=0f) {
                                        c.drawLine(xx2+ bitmap_point.getWidth() / 2,yy2+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }else if (yy2==0f&&yy3==0f&&yy4==0f&&yy5==0f&&yy6==0f&&yy7==0f&&yy8!=0f&&yy1!=0f) {
                                        c.drawLine(xx1+ bitmap_point.getWidth() / 2,yy1+ bitmap_point.getWidth() / 2,xx8+ bitmap_point.getWidth() / 2,yy8+ bitmap_point.getWidth() / 2,huaxian_text); 
                                         }              
                                } 
                          
                }
                // ---------------------------------------------------------血压结束-----------------------------------------------
                break;
    
            case 0:
                
                String Weekdayxueya[] = new String[2];
                float xx1=0f,xx2=0f,xx3=0f,xx4=0f,xx5=0f,xx6=0f,xx7=0f,xx8=0f,x00=0f,x01=0f;
                float yy1=0f,yy2=0f,yy3=0f,yy4=0f,yy5=0f,yy6=0f,yy7=0f,yy8=0f,y00=0f,y01=0f;          
                 //   System.out.print(dateNow+"fffffffffffffff");      
                 //  System.out.print("mmmmmmm"+Weekdayxueya[i]+"\n");

                    cl.setTime(dateNow);                 
                    cl.add(Calendar.DAY_OF_MONTH, 0);
                    Weekdayxueya[0] = format2.format(cl.getTime());
                //    long timetoday=Long.parseLong(Weekdayxueya[0]);
                    final List<String> li = uService.Findwendu2(Weekdayxueya[0],MainActivity.NAME);  
                    System.out.print("sssssssssssssssssssss"+li.size()+"\n");
                 //   if (Weekdayxueya[0].equals(li.get(1).substring(0, 8)))
                 if (li.size()>0) {
                	 c.drawBitmap(bitmap_point,interval_left, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(0).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 , null); 
                	 c.drawText(li.get(0).substring(0, 4), interval_left-30, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(0).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 -20, paintxy_text); 
                	 for (int i = 0; i < li.size()-1; i++) {
                         //    Log.e("mjjjjjjjjjjjjjj", "dddddddddddd");
                                      x00=interval_left*(i)*8;
                                      x01=interval_left*(i+1)*8;
                                      y00=(getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2;
                                      y01=(getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i+1).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2;
                                     
                                      c.drawBitmap(bitmap_point,interval_left+interval_left*(i)*8, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 , null); 
                                     c.drawBitmap(bitmap_point,interval_left+interval_left*(i+1)*8, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i+1).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 , null); 
                                      c.drawLine(x00+interval_left+ bitmap_point.getWidth() / 2,y00+bitmap_point.getHeight() / 2,x01+interval_left+ bitmap_point.getWidth() / 2,y01+bitmap_point.getHeight() / 2,huaxian_text);  
                                      c.drawText(li.get(i).substring(0, 4), interval_left+interval_left*(i)*8-30, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 -20, paintxy_text);
                                      c.drawText(li.get(i+1).substring(0, 4), interval_left+interval_left*(i+1)*8-30, (getHeight()- buttom - rate1 * ((Float.parseFloat( li.get(i+1).substring(0, 4))-35f))*10)- bitmap_point.getHeight() / 2 -20, paintxy_text);
                                 //   setLayoutParams(new LayoutParams(StaticValue.surfaceviewWidth+interval_left+interval_left*8,LayoutParams.MATCH_PARENT));
                                //    invalidate();
                         }
                }
                 cl.setTime(dateNow);                 
                 cl.add(Calendar.DAY_OF_MONTH, 0);
                 Weekdayxueya[0] = format2.format(cl.getTime());
                 final List<String> lii = uService.Findwendu3(Weekdayxueya[0],MainActivity.NAME); 
                 if (lii.size()>0) {
                     for (int i = 0; i < lii.size()-1; i++) {
                      //   Log.e("mjjjjjjjjjjjjjj", "dddddddddddd");
                             /*     x00=interval_left*(i)*8;
                                  x01=interval_left*(i+1)*8; */            
                                  c.drawText(lii.get(i).substring(8, 10)+":"+lii.get(i).substring(10, 12), interval_left+interval_left*(i)*8-30, (getHeight()- buttom/4 ), paintxy_text);
                                //  c.drawText(lii.get(i).substring(4, 6)+"-"+lii.get(i).substring(6, 8), interval_left+interval_left*(i)*8-30, (getHeight()- buttom-10f ), paintxy_text);
                                  c.drawText(lii.get(i+1).substring(8, 10)+":"+lii.get(i+1).substring(10, 12), interval_left+interval_left*(i+1)*8-30, (getHeight()- buttom/4 ), paintxy_text);
                     }
                }
                break;                 
           
/*            case 3:
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

           
                break;*/
            default:
                break;

        }

    }
    
    
   /* public static int getmax(int[] arr){
        int max=arr[0];
        for(int i=1;i<arr.length;i++){
            if(arr[i]>max){
                max=arr[i];
            }
        }
        return max;
    }*/
   
    public static float getmax(float[] arr){
        float max=arr[0];
        for(int i=1;i<arr.length;i++){
            if(arr[i]>max){
                max=arr[i];
            }
        }
        return max;
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

        String fengjie_linexueya[] ={
        "35","36","37","38","39","40","基础体温（℃）","低体温区间","高体温区间","排卵前体温<36.6℃，排卵后体温36.6℃~37.0℃"};
        switch (flagtag) {

            case 9:
                c.drawText(fengjie_linexueya[0], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 0.125f+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[1], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 2+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[2], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 4+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[3], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 6+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[4], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 8+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[5], 5+interval_left+interval_kedu, getHeight() - buttom - interval * 10+paint_percent_size/2, paint_percent);
                c.drawText(fengjie_linexueya[6], 5+interval_left+interval_kedu+100, getHeight() - buttom - interval * 10+paint_percent_size/2+50, jichutiwen);
               c.drawText(fengjie_linexueya[7], 5+interval_left+interval_kedu+350, getHeight() - buttom - interval * 2+paint_percent_size/2-(((getHeight() - buttom - top)/50f)*2), pailuanqi);
                c.drawText(fengjie_linexueya[8], 5+interval_left+interval_kedu+350, getHeight() - buttom - interval * 3+paint_percent_size/2-(((getHeight() - buttom - top)/50f)*3), pailuanqi);
          //      c.drawText(fengjie_linexueya[9], 5+interval_left+interval_kedu+50, getHeight() - buttom - interval * 10+paint_percent_size/2-25, nvxingtiwen);
                break;
            default:
                break;
        }

    }
}
