package com.HWDTEMPT.view;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.HWDTEMPT.hwdtempt.MainActivity;
import com.HWDTEMPT.hwdtempt.R;
import com.HWDTEMPT.hwdtempt.bdlishiactivity;
import com.HWDTEMPT.hwdtempt.bloodactivity;
import com.HWDTEMPT.hwdtempt.bloodknowledge;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.StaticValue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; 
import java.util.List;

public class MyCircleView extends View {

    private String u;
    public static Handler mleHandler;  
    public static final int HATA1 =11;
    public static final int HATA23 =12;
    public static final int HATA12 =13;    
    public static final int HATA3 =14; 
    public static final int HATA123 =15; 
    private TextView height_rate_data;
    private Paint linePaint;
    private Paint circlePaint;
    private Paint textPaint;//刻度值
    private Paint centerTextPaint,zhengchanglvse,gaowenchengse,shijian;//中间的温度值
    private Paint indicatorPaint;//指示器
    private RectF mRectF;
    private Bitmap bitmap_point;
    
    private int defaultValue;
    int mCenter = 0;// 圆的半径
    int mRadius = 0;
    private SweepGradient mSweepGradient,mSweepGradient2;
    private int scanDegree = 0;//最高温度和最低温度扫描角度
    private float currentScanDegree=0;//当前温度扫过的角度
    private boolean isCanMove;
    private GetDegreeInterface mGetDegreeInterface;
    int minDegrees = 0;//最低温度
    int maxDegree=0;//最高温度
    float currentDegree=0;//当前温度
    public static boolean wendujieshu=true;
    public boolean flag=false;
    int screenWidth,screenHeight;
   
    
    public MyCircleView(Context context) {
        super(context);
    //    Log.e("My----->", "1");
        initPaint();

    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        screenWidth=MeasureUtil.getScreenWidth(context);
     //   screenHeight=MeasureUtil.getScreenHeight(context)+600;
        Log.e("My----->", "2  "+screenWidth+"  "+screenHeight);
        if (screenWidth > 1500) {
            screenHeight=MeasureUtil.getScreenHeight(context)+600;
        }else if(screenWidth <1100&&screenWidth > 1000){
            screenHeight=MeasureUtil.getScreenHeight(context)+100;
        }else {
            screenHeight=MeasureUtil.getScreenHeight(context);
        }
        initPaint(); 
        
    }

    private void initPaint() {
       
    	 if (screenWidth > 1500) {
    		 linePaint = new Paint();
    	        linePaint.setColor(Color.CYAN);
    	        linePaint.setStyle(Style.FILL);
    	        linePaint.setAntiAlias(true);
    	        linePaint.setStrokeWidth(1.0f);

    	        textPaint = new Paint();
    	        textPaint.setColor(Color.BLACK);
    	        textPaint.setTextAlign(Paint.Align.CENTER);
    	        textPaint.setAntiAlias(true);
    	        textPaint.setTextSize(45);

    	        centerTextPaint = new Paint();
    	        centerTextPaint.setColor(Color.BLUE);
    	        centerTextPaint.setTextAlign(Paint.Align.CENTER);
    	        centerTextPaint.setAntiAlias(true);
    	        centerTextPaint.setTextSize(100);
    	   
    	        zhengchanglvse = new Paint();
    	        zhengchanglvse.setColor(Color.BLACK);
    	        zhengchanglvse.setTextAlign(Paint.Align.CENTER);
    	        zhengchanglvse.setAntiAlias(true);
    	        zhengchanglvse.setTextSize(150);
    	      
    	        gaowenchengse = new Paint();
    	        gaowenchengse.setColor(Color.RED);
    	        gaowenchengse.setTextAlign(Paint.Align.CENTER);
    	        gaowenchengse.setAntiAlias(true);
    	        gaowenchengse.setTextSize(150);
    	        
    	        shijian = new Paint();
    	        shijian.setColor(Color.BLACK);
    	        shijian.setTextAlign(Paint.Align.CENTER);
    	        shijian.setAntiAlias(true);
    	        shijian.setTextSize(50);
    	        
    	        circlePaint = new Paint();
    	        circlePaint.setColor(Color.WHITE);
    	        circlePaint.setAntiAlias(true);
    	        circlePaint.setStyle(Paint.Style.STROKE);
    	        circlePaint.setStrokeCap(Cap.ROUND);//实现末端圆弧
    	        circlePaint.setStrokeWidth(90.0f);
    	        
    	        indicatorPaint=new Paint();
    	        indicatorPaint.setColor(Color.BLACK);
    	        indicatorPaint.setAntiAlias(true);
    	        indicatorPaint.setStyle(Style.FILL);
    	        linePaint.setStrokeWidth(5.0f);
         }else if(screenWidth <1100&&screenWidth > 1000){
        	 linePaint = new Paint();
             linePaint.setColor(Color.CYAN);
             linePaint.setStyle(Style.FILL);
             linePaint.setAntiAlias(true);
             linePaint.setStrokeWidth(1.0f);

             textPaint = new Paint();
             textPaint.setColor(Color.BLACK);
             textPaint.setTextAlign(Paint.Align.CENTER);
             textPaint.setAntiAlias(true);
             textPaint.setTextSize(35);

             centerTextPaint = new Paint();
             centerTextPaint.setColor(Color.BLUE);
             centerTextPaint.setTextAlign(Paint.Align.CENTER);
             centerTextPaint.setAntiAlias(true);
             centerTextPaint.setTextSize(100);
        
             zhengchanglvse = new Paint();
             zhengchanglvse.setColor(Color.BLACK);
             zhengchanglvse.setTextAlign(Paint.Align.CENTER);
             zhengchanglvse.setAntiAlias(true);
             zhengchanglvse.setTextSize(120);
           
             gaowenchengse = new Paint();
             gaowenchengse.setColor(Color.RED);
             gaowenchengse.setTextAlign(Paint.Align.CENTER);
             gaowenchengse.setAntiAlias(true);
             gaowenchengse.setTextSize(120);
             
             shijian = new Paint();
             shijian.setColor(Color.BLACK);
             shijian.setTextAlign(Paint.Align.CENTER);
             shijian.setAntiAlias(true);
             shijian.setTextSize(35);
             
             circlePaint = new Paint();
             circlePaint.setColor(Color.WHITE);
             circlePaint.setAntiAlias(true);
             circlePaint.setStyle(Paint.Style.STROKE);
             circlePaint.setStrokeCap(Cap.ROUND);//实现末端圆弧
             circlePaint.setStrokeWidth(90.0f);
             
             indicatorPaint=new Paint();
             indicatorPaint.setColor(Color.BLACK);
             indicatorPaint.setAntiAlias(true);
             indicatorPaint.setStyle(Style.FILL);
             linePaint.setStrokeWidth(5.0f);
         }else {
        	 linePaint = new Paint();
             linePaint.setColor(Color.CYAN);
             linePaint.setStyle(Style.FILL);
             linePaint.setAntiAlias(true);
             linePaint.setStrokeWidth(1.0f);

             textPaint = new Paint();
             textPaint.setColor(Color.BLACK);
             textPaint.setTextAlign(Paint.Align.CENTER);
             textPaint.setAntiAlias(true);
             textPaint.setTextSize(45);

             centerTextPaint = new Paint();
             centerTextPaint.setColor(Color.BLUE);
             centerTextPaint.setTextAlign(Paint.Align.CENTER);
             centerTextPaint.setAntiAlias(true);
             centerTextPaint.setTextSize(100);
        
             zhengchanglvse = new Paint();
             zhengchanglvse.setColor(Color.BLACK);
             zhengchanglvse.setTextAlign(Paint.Align.CENTER);
             zhengchanglvse.setAntiAlias(true);
             zhengchanglvse.setTextSize(150);
           
             gaowenchengse = new Paint();
             gaowenchengse.setColor(Color.RED);
             gaowenchengse.setTextAlign(Paint.Align.CENTER);
             gaowenchengse.setAntiAlias(true);
             gaowenchengse.setTextSize(150);
             
             shijian = new Paint();
             shijian.setColor(Color.BLACK);
             shijian.setTextAlign(Paint.Align.CENTER);
             shijian.setAntiAlias(true);
             shijian.setTextSize(50);
             
             circlePaint = new Paint();
             circlePaint.setColor(Color.WHITE);
             circlePaint.setAntiAlias(true);
             circlePaint.setStyle(Paint.Style.STROKE);
             circlePaint.setStrokeCap(Cap.ROUND);//实现末端圆弧
             circlePaint.setStrokeWidth(90.0f);
             
             indicatorPaint=new Paint();
             indicatorPaint.setColor(Color.BLACK);
             indicatorPaint.setAntiAlias(true);
             indicatorPaint.setStyle(Style.FILL);
             linePaint.setStrokeWidth(5.0f);
             
             
         }
    	
      
  
        
        // 着色的共有270度，这里设置了12个颜色均分360度s
        int[] colors = {0xFF1E90FF,0xFF00FF00,0xFFFF8C00,0xFFD52B2B,0xFFD52B2B,0xFFFFFFFF, 0xFFFFFFFF,
                0xFF6AE2FD,0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF1E90FF};
        
        mCenter = screenWidth / 2;
        mRadius = screenWidth/ 2 - 100;
        // 渐变色
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, null);
        // 构建圆的外切矩形
        mRectF = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter
                + mRadius, mCenter + mRadius);
    }
/*    public void setUser(String user){
       
        this.u=user;

        }*/
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        
      
        circlePaint.setShader(null);
        
        canvas.drawArc(mRectF, 135, 270, false, circlePaint);
        
   //     Log.e("scan--1-->", scanDegree+"");
        //重新赋值了，所以手指滑动没有显示
        scanDegree=(getMaxDegree()-getMinDegree())*3;
        // 设置画笔渐变色
        circlePaint.setShader(mSweepGradient);
        canvas.drawArc(mRectF, 135+(getMinDegree()-10)*9, (float) scanDegree, false, circlePaint);
        
        int insideIndicator=mRadius-60;//离圆环的距离
        currentScanDegree=(getCurrentDegree()-10)*3;
        BigDecimal   b  =   new BigDecimal((currentScanDegree/9+10));  
        float f1 =b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
        
        String Weekdayxueya[] = new String[1];
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddhhmm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar cl = Calendar.getInstance();
   /*     cl.setTime(dateNow);                 
        cl.add(Calendar.DAY_OF_MONTH, 0);
        Weekdayxueya[0] = format2.format(cl.getTime());
        final List<String> lii = uService.Findwendu3(Weekdayxueya[0],MainActivity.NAME); 
        if (lii.size()>0) {
            for (int i = 0; i < lii.size()-1; i++) {
                Log.e("mjjjjjjjjjjjjjj", "dddddddddddd");
                canvas.drawText(lii.get(i).substring(4, 6)+"-"+lii.get(i).substring(6, 8)+" "+lii.get(i).substring(8, 10)+":"+lii.get(i).substring(10, 12), mCenter, mCenter+100, shijian);
            }
       }*/

        if (bloodactivity.wenduchuanzhi) {
           if (35.9f<=f1&&f1<=36.4f) {
               canvas.drawText(f1+"℃", mCenter, mCenter, zhengchanglvse);
               canvas.drawText("低体温区", mCenter, mCenter+300, zhengchanglvse);
               Date dateNow1 = new Date();
               String ssString=format1.format(dateNow1);
               canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
               Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
        }  else if (36.6f<=f1&&f1<=37.1f) {
            canvas.drawText(f1+"℃", mCenter, mCenter, zhengchanglvse);
            canvas.drawText("高体温区", mCenter, mCenter+300, zhengchanglvse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
       }  else if (36.5f==f1) {
           canvas.drawText(f1+"℃", mCenter, mCenter, zhengchanglvse);
        //   canvas.drawText("高体温区", mCenter, mCenter+300, zhengchanglvse);
           Date dateNow1 = new Date();
           String ssString=format1.format(dateNow1);
           canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
           Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
      } 
        else if (f1>37.2f&&f1<38.0f) {
            canvas.drawText(f1+"℃", mCenter, mCenter, gaowenchengse);
            canvas.drawText("低烧", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1>37.9f&&f1<39.0f) {
            canvas.drawText(f1+"℃", mCenter, mCenter, gaowenchengse);
            canvas.drawText("中度发烧", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1>38.9f) {
            canvas.drawText(f1+"℃", mCenter, mCenter, gaowenchengse);
            canvas.drawText("高烧", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1<35.0f) {
            canvas.drawText(f1+"℃", mCenter, mCenter, centerTextPaint);
            canvas.drawText("温度偏低，请重新测量", mCenter, mCenter+300, centerTextPaint);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
        }
  
        }else {
            canvas.drawText(0.0+"℃", mCenter, mCenter, zhengchanglvse);
        }
       
        if ((currentScanDegree/9+10)>15) {
            wendujieshu=false;
            Log.e("3333333333333", "ttttttttttt");
        }
        //全温度盘时，把currentScanDegree改为scanDegree，中间的值就会跟着改变了
       // Log.e("指示角度----》", currentScanDegree+"");
        if (currentScanDegree<=45) {//第三象限
            canvas.drawCircle((float)(mCenter-insideIndicator*Math.sin(Math.PI*(currentScanDegree+45)/180)),(float)(mCenter+insideIndicator*Math.cos(Math.PI*(currentScanDegree+45)/180)), 15, indicatorPaint);
        }else if(45<currentScanDegree&&currentScanDegree<=135) {//第二象限
            canvas.drawCircle((float)(mCenter-insideIndicator*Math.cos(Math.PI*(currentScanDegree-45)/180)),(float)(mCenter-insideIndicator*Math.sin(Math.PI*(currentScanDegree-45)/180)), 15, indicatorPaint);
        }else if (135<currentScanDegree&&currentScanDegree<=225) {//第一象限
            canvas.drawCircle((float)(mCenter+insideIndicator*Math.sin(Math.PI*(currentScanDegree-135)/180)),(float)(mCenter-insideIndicator*Math.cos(Math.PI*(currentScanDegree-135)/180)), 15, indicatorPaint);
        }else if(225<currentScanDegree&&currentScanDegree<=270){//第四象限
            canvas.drawCircle((float)(mCenter+insideIndicator*Math.cos(Math.PI*(currentScanDegree-225)/180)),(float)(mCenter+insideIndicator*Math.sin(Math.PI*(currentScanDegree-225)/180)), 15, indicatorPaint);
        }
        
        for (int i = 0; i < 120; i++) {
            if (i <= 45 || i >= 75) {
                canvas.drawLine(mCenter, mCenter - mRadius - 30, mCenter,
                        mCenter - mRadius + 30, linePaint);
            }
            canvas.rotate(3, mCenter, mCenter);
        }
        
        // x代表文字的x轴距离圆心x轴的距离 因为刚好是45度，所以文字x轴值和y值相等
        int x = 0;
        // 三角形的斜边
        int c = mRadius + 60 / 2 + 40;// 40代表这个字距离圆外边的距离
        // 因为是每45度写一次文字，故根据到圆心的位置，利用三角形的公式，可以算出文字的坐标值
        x = (int) Math.sqrt((c * c / 2));

//      if (getMinDegree() == 0) {
//          minDegrees = 10;
//      }
/*        canvas.drawText("10", mCenter - x, mCenter + x, textPaint);
        canvas.drawText("15", mCenter - c, mCenter,
                textPaint);
        canvas.drawText("20", mCenter - x, mCenter - x,
                textPaint);
        canvas.drawText("25", mCenter, mCenter - c,
                textPaint);
        canvas.drawText( "30", mCenter + x, mCenter - x,
                textPaint);*/
 /*       canvas.drawText( "35", mCenter + c, mCenter,
                textPaint);
        canvas.drawText( "40", mCenter + x, mCenter + x,
                textPaint);*/
        
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO 自动生成的方法存根
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            isCanMove = true;
            break;
        case MotionEvent.ACTION_MOVE:
            float x = event.getX();
            float y = event.getY();
            float StartX = event.getX();
            float StartY = event.getY();
            // 判断当前手指距离圆心的距离 如果大于mCenter代表在圆心的右侧
            if (x > mCenter) {
                x = x - mCenter;
            } else {
                x = mCenter - x;
            }
            if (y > mCenter) {
                y = y - mCenter;
            } else {
                y = mCenter - y;
            }
            // 判断当前手指是否在圆环上的（30+10多加了10个像素）
            if ((mRadius + 40) < Math.sqrt(x * x + y * y)
                    || Math.sqrt(x * x + y * y) < (mRadius - 40)) {
             //   Log.e("cmos---->", "终止滑动");
                isCanMove = false;
                return false;
            }
            float cosValue = x / (float) Math.sqrt(x * x + y * y);
            // 根据cosValue求角度值
            double acos = Math.acos(cosValue);// 弧度值
            acos = Math.toDegrees(acos);// 角度值

            if (StartX > mCenter && StartY < mCenter) {
                acos = 360 - acos;// 第一象限
             //   Log.e("象限---->", "第一象限");
            } else if (StartX < mCenter && StartY < mCenter) {
                acos = 180 + acos;// 第二象限
             //   Log.e("象限---->", "第二象限");
            } else if (StartX < mCenter && StartY > mCenter) {
                acos = 180 - acos;// 第三象限
            //    Log.e("象限---->", "第三象限");
            } else {
                // acos=acos;
          //      Log.e("象限---->", "第四象限");
            }
        //    Log.e("旋转的角度---->", acos + "");
            scanDegree = (int) acos;
            if (scanDegree >= 135 && scanDegree < 360) {
                scanDegree = scanDegree - 135;
                int actualDegree = (int) (scanDegree / 9);
                if (mGetDegreeInterface != null) {
                    mGetDegreeInterface.getActualDegree(actualDegree
                            + minDegrees);
                }
            } else if (scanDegree <= 45) {
                scanDegree = (int) (180 + 45 + acos);
                int actualDegree = (int) (scanDegree / 9);
                if (mGetDegreeInterface != null) {
                    mGetDegreeInterface.getActualDegree(actualDegree
                            + minDegrees);
                }
            } else {
                return false;
            }
            postInvalidate();
            return true;
        }
        return true;
    }
*/

  
    
    /**
     * 设置最低温度
     * 
     * @param degree
     */
    public void setMinDegree(int degree) {
        this.minDegrees = degree;
    }

    public int getMinDegree() {
        return minDegrees;
    }

    /**
     * 获取当前温度值接口
     *
     */
    public interface GetDegreeInterface {
        
        void getActualDegree(float degree);
       
    }

    public void setGetDegreeInterface(GetDegreeInterface arg) {
        this.mGetDegreeInterface = arg;
    }

    /**
     * 设置最高温度值
     */
    public void setMaxDegree(int degree){
        this.maxDegree=degree;
    }
    
    public int getMaxDegree() {
        return maxDegree;
    }
    
    /**
     * 设置当前温度
     * @param currentDegree
     */
   /* public void MyView(Context context, AttributeSet abu)
    { 
        super(context, abu); 
        bloodactivity a = (bloodactivity) context;
        ArrayList<String> lists = new ArrayList<String>(); 
        lists=a.getIntent(); 
    }
    */
    public void setCurrentDegree(float currentDegree) {
        
        this.currentDegree = currentDegree;
    }
    
    public float getCurrentDegree() {
        return currentDegree;
    }
    
    // 因为自定义的空间的高度设置的是wrap_content,所以我们必须要重写onMeasure方法去测量高度，否则布局界面看不到
    // 其他控件(被覆盖)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        // 默认宽高;
        defaultValue=screenWidth;
        
        switch (mode) {
        case MeasureSpec.AT_MOST:
            // 最大值模式 当控件的layout_Width或layout_height属性指定为wrap_content时
            Log.e("cmos---->", "size " + size + " screenWidth " + screenWidth);
            size = Math.min(defaultValue, size);
            break;
        case MeasureSpec.EXACTLY:
            // 精确值模式
            // 当控件的android:layout_width=”100dp”或android:layout_height=”match_parent”时

            break;
        default:
            size = defaultValue;
            break;
        }
        defaultValue = size;
        return size;
    }

    /**
     * 测量高度
     * 
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
        case MeasureSpec.AT_MOST:
            // 最大值模式 当控件的layout_Width或layout_height属性指定为wrap_content时
            Log.e("cmos---->", "size " + size + " screenHeight " + screenHeight);
            size = Math.min(screenHeight/2, size);
            break;
        case MeasureSpec.EXACTLY:
            // 精确值模式
            // 当控件的android:layout_width=”100dp”或android:layout_height=”match_parent”时

            break;
        default:
            size = defaultValue;
            break;
        }
        return size;
    }

}