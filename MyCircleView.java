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
    private Paint textPaint;//�̶�ֵ
    private Paint centerTextPaint,zhengchanglvse,gaowenchengse,shijian;//�м���¶�ֵ
    private Paint indicatorPaint;//ָʾ��
    private RectF mRectF;
    private Bitmap bitmap_point;
    
    private int defaultValue;
    int mCenter = 0;// Բ�İ뾶
    int mRadius = 0;
    private SweepGradient mSweepGradient,mSweepGradient2;
    private int scanDegree = 0;//����¶Ⱥ�����¶�ɨ��Ƕ�
    private float currentScanDegree=0;//��ǰ�¶�ɨ���ĽǶ�
    private boolean isCanMove;
    private GetDegreeInterface mGetDegreeInterface;
    int minDegrees = 0;//����¶�
    int maxDegree=0;//����¶�
    float currentDegree=0;//��ǰ�¶�
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
    	        circlePaint.setStrokeCap(Cap.ROUND);//ʵ��ĩ��Բ��
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
             circlePaint.setStrokeCap(Cap.ROUND);//ʵ��ĩ��Բ��
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
             circlePaint.setStrokeCap(Cap.ROUND);//ʵ��ĩ��Բ��
             circlePaint.setStrokeWidth(90.0f);
             
             indicatorPaint=new Paint();
             indicatorPaint.setColor(Color.BLACK);
             indicatorPaint.setAntiAlias(true);
             indicatorPaint.setStyle(Style.FILL);
             linePaint.setStrokeWidth(5.0f);
             
             
         }
    	
      
  
        
        // ��ɫ�Ĺ���270�ȣ�����������12����ɫ����360��s
        int[] colors = {0xFF1E90FF,0xFF00FF00,0xFFFF8C00,0xFFD52B2B,0xFFD52B2B,0xFFFFFFFF, 0xFFFFFFFF,
                0xFF6AE2FD,0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF, 0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF8CD0E5, 0xFFFFFFFF,0xFF1E90FF};
        
        mCenter = screenWidth / 2;
        mRadius = screenWidth/ 2 - 100;
        // ����ɫ
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, null);
        // ����Բ�����о���
        mRectF = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter
                + mRadius, mCenter + mRadius);
    }
/*    public void setUser(String user){
       
        this.u=user;

        }*/
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO �Զ����ɵķ������
        super.onDraw(canvas);
        
      
        circlePaint.setShader(null);
        
        canvas.drawArc(mRectF, 135, 270, false, circlePaint);
        
   //     Log.e("scan--1-->", scanDegree+"");
        //���¸�ֵ�ˣ�������ָ����û����ʾ
        scanDegree=(getMaxDegree()-getMinDegree())*3;
        // ���û��ʽ���ɫ
        circlePaint.setShader(mSweepGradient);
        canvas.drawArc(mRectF, 135+(getMinDegree()-10)*9, (float) scanDegree, false, circlePaint);
        
        int insideIndicator=mRadius-60;//��Բ���ľ���
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
               canvas.drawText(f1+"��", mCenter, mCenter, zhengchanglvse);
               canvas.drawText("��������", mCenter, mCenter+300, zhengchanglvse);
               Date dateNow1 = new Date();
               String ssString=format1.format(dateNow1);
               canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
               Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
        }  else if (36.6f<=f1&&f1<=37.1f) {
            canvas.drawText(f1+"��", mCenter, mCenter, zhengchanglvse);
            canvas.drawText("��������", mCenter, mCenter+300, zhengchanglvse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
       }  else if (36.5f==f1) {
           canvas.drawText(f1+"��", mCenter, mCenter, zhengchanglvse);
        //   canvas.drawText("��������", mCenter, mCenter+300, zhengchanglvse);
           Date dateNow1 = new Date();
           String ssString=format1.format(dateNow1);
           canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
           Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
      } 
        else if (f1>37.2f&&f1<38.0f) {
            canvas.drawText(f1+"��", mCenter, mCenter, gaowenchengse);
            canvas.drawText("����", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1>37.9f&&f1<39.0f) {
            canvas.drawText(f1+"��", mCenter, mCenter, gaowenchengse);
            canvas.drawText("�жȷ���", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1>38.9f) {
            canvas.drawText(f1+"��", mCenter, mCenter, gaowenchengse);
            canvas.drawText("����", mCenter, mCenter+300, gaowenchengse);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");   
        }else if (f1<35.0f) {
            canvas.drawText(f1+"��", mCenter, mCenter, centerTextPaint);
            canvas.drawText("�¶�ƫ�ͣ������²���", mCenter, mCenter+300, centerTextPaint);
            Date dateNow1 = new Date();
            String ssString=format1.format(dateNow1);
            canvas.drawText(ssString.substring(4, 6)+"-"+ssString.substring(6, 8)+" "+ssString.substring(8, 10)+":"+ssString.substring(10, 12), mCenter, mCenter+100, shijian);
            Log.e("vvvvvvvvv", currentScanDegree/9+10+"");    
        }
  
        }else {
            canvas.drawText(0.0+"��", mCenter, mCenter, zhengchanglvse);
        }
       
        if ((currentScanDegree/9+10)>15) {
            wendujieshu=false;
            Log.e("3333333333333", "ttttttttttt");
        }
        //ȫ�¶���ʱ����currentScanDegree��ΪscanDegree���м��ֵ�ͻ���Ÿı���
       // Log.e("ָʾ�Ƕ�----��", currentScanDegree+"");
        if (currentScanDegree<=45) {//��������
            canvas.drawCircle((float)(mCenter-insideIndicator*Math.sin(Math.PI*(currentScanDegree+45)/180)),(float)(mCenter+insideIndicator*Math.cos(Math.PI*(currentScanDegree+45)/180)), 15, indicatorPaint);
        }else if(45<currentScanDegree&&currentScanDegree<=135) {//�ڶ�����
            canvas.drawCircle((float)(mCenter-insideIndicator*Math.cos(Math.PI*(currentScanDegree-45)/180)),(float)(mCenter-insideIndicator*Math.sin(Math.PI*(currentScanDegree-45)/180)), 15, indicatorPaint);
        }else if (135<currentScanDegree&&currentScanDegree<=225) {//��һ����
            canvas.drawCircle((float)(mCenter+insideIndicator*Math.sin(Math.PI*(currentScanDegree-135)/180)),(float)(mCenter-insideIndicator*Math.cos(Math.PI*(currentScanDegree-135)/180)), 15, indicatorPaint);
        }else if(225<currentScanDegree&&currentScanDegree<=270){//��������
            canvas.drawCircle((float)(mCenter+insideIndicator*Math.cos(Math.PI*(currentScanDegree-225)/180)),(float)(mCenter+insideIndicator*Math.sin(Math.PI*(currentScanDegree-225)/180)), 15, indicatorPaint);
        }
        
        for (int i = 0; i < 120; i++) {
            if (i <= 45 || i >= 75) {
                canvas.drawLine(mCenter, mCenter - mRadius - 30, mCenter,
                        mCenter - mRadius + 30, linePaint);
            }
            canvas.rotate(3, mCenter, mCenter);
        }
        
        // x�������ֵ�x�����Բ��x��ľ��� ��Ϊ�պ���45�ȣ���������x��ֵ��yֵ���
        int x = 0;
        // �����ε�б��
        int c = mRadius + 60 / 2 + 40;// 40��������־���Բ��ߵľ���
        // ��Ϊ��ÿ45��дһ�����֣��ʸ��ݵ�Բ�ĵ�λ�ã����������εĹ�ʽ������������ֵ�����ֵ
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
        // TODO �Զ����ɵķ������
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            isCanMove = true;
            break;
        case MotionEvent.ACTION_MOVE:
            float x = event.getX();
            float y = event.getY();
            float StartX = event.getX();
            float StartY = event.getY();
            // �жϵ�ǰ��ָ����Բ�ĵľ��� �������mCenter������Բ�ĵ��Ҳ�
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
            // �жϵ�ǰ��ָ�Ƿ���Բ���ϵģ�30+10�����10�����أ�
            if ((mRadius + 40) < Math.sqrt(x * x + y * y)
                    || Math.sqrt(x * x + y * y) < (mRadius - 40)) {
             //   Log.e("cmos---->", "��ֹ����");
                isCanMove = false;
                return false;
            }
            float cosValue = x / (float) Math.sqrt(x * x + y * y);
            // ����cosValue��Ƕ�ֵ
            double acos = Math.acos(cosValue);// ����ֵ
            acos = Math.toDegrees(acos);// �Ƕ�ֵ

            if (StartX > mCenter && StartY < mCenter) {
                acos = 360 - acos;// ��һ����
             //   Log.e("����---->", "��һ����");
            } else if (StartX < mCenter && StartY < mCenter) {
                acos = 180 + acos;// �ڶ�����
             //   Log.e("����---->", "�ڶ�����");
            } else if (StartX < mCenter && StartY > mCenter) {
                acos = 180 - acos;// ��������
            //    Log.e("����---->", "��������");
            } else {
                // acos=acos;
          //      Log.e("����---->", "��������");
            }
        //    Log.e("��ת�ĽǶ�---->", acos + "");
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
     * ��������¶�
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
     * ��ȡ��ǰ�¶�ֵ�ӿ�
     *
     */
    public interface GetDegreeInterface {
        
        void getActualDegree(float degree);
       
    }

    public void setGetDegreeInterface(GetDegreeInterface arg) {
        this.mGetDegreeInterface = arg;
    }

    /**
     * ��������¶�ֵ
     */
    public void setMaxDegree(int degree){
        this.maxDegree=degree;
    }
    
    public int getMaxDegree() {
        return maxDegree;
    }
    
    /**
     * ���õ�ǰ�¶�
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
    
    // ��Ϊ�Զ���Ŀռ�ĸ߶����õ���wrap_content,�������Ǳ���Ҫ��дonMeasure����ȥ�����߶ȣ����򲼾ֽ��濴����
    // �����ؼ�(������)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * �������
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        // Ĭ�Ͽ��;
        defaultValue=screenWidth;
        
        switch (mode) {
        case MeasureSpec.AT_MOST:
            // ���ֵģʽ ���ؼ���layout_Width��layout_height����ָ��Ϊwrap_contentʱ
            Log.e("cmos---->", "size " + size + " screenWidth " + screenWidth);
            size = Math.min(defaultValue, size);
            break;
        case MeasureSpec.EXACTLY:
            // ��ȷֵģʽ
            // ���ؼ���android:layout_width=��100dp����android:layout_height=��match_parent��ʱ

            break;
        default:
            size = defaultValue;
            break;
        }
        defaultValue = size;
        return size;
    }

    /**
     * �����߶�
     * 
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
        case MeasureSpec.AT_MOST:
            // ���ֵģʽ ���ؼ���layout_Width��layout_height����ָ��Ϊwrap_contentʱ
            Log.e("cmos---->", "size " + size + " screenHeight " + screenHeight);
            size = Math.min(screenHeight/2, size);
            break;
        case MeasureSpec.EXACTLY:
            // ��ȷֵģʽ
            // ���ؼ���android:layout_width=��100dp����android:layout_height=��match_parent��ʱ

            break;
        default:
            size = defaultValue;
            break;
        }
        return size;
    }

}