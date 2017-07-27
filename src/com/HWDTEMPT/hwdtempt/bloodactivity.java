package com.HWDTEMPT.hwdtempt;

import android.Manifest;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.HWDTEMPT.hwdtempt.R.color;
import com.HWDTEMPT.Util.DialogUtil;
import com.HWDTEMPT.model.FindingPots;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.tool.Analyse;
import com.HWDTEMPT.tool.FindR;
import com.HWDTEMPT.tool.RawDataArray;
import com.HWDTEMPT.view.CircularSeekBar;
import com.HWDTEMPT.view.wenduhomediagram;
import com.HWDTEMPT.view.MyCircleView.GetDegreeInterface;
import com.HWDTEMPT.view.MyCircleView;
import com.huicheng.ui.BluetoothLeService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class bloodactivity extends Activity implements GetDegreeInterface, OnClickListener{

    public static boolean fanhuixy=false;
    long exitTime=0;
    RelativeLayout linear0;//图表界面
    private TextView TextA, TextB, TextC, TextD;//周月季年
    private SimpleDateFormat format,format2;//时间格式化
    HashMap<Long, String> lists0 = new HashMap<Long, String>();
    final int XINLV = 0, FANGXINGZAOBO = 1, SHIXINGZAOBO = 2, JIAOJIEXINGZAOBO = 3, XINLVBUQI = 4,
            SHIXINGXINDONGGUOSU = 5, FANGXINGYIBO = 6, SHIXINGYIBO = 7, FANGCHAN = 8,XUEYA=9;
//    final float wendu=10;
    final int DAY = 0, ZHOU = 1, YUE = 2, JI = 3;
    
    private MyCircleView myCircleView;
    private TextView actualDegree;
    private Button clearBt,allBt,okBt;
    private EditText minDegreEditText,maxDegreEditText,cureentDegreEditText;
     String max = "";
     String min = "";
     String current = "";
       
    BluetoothLeService mBLEservice;
    BluetoothAdapter mBluetoothAdapter;
    //private String deviceName, deviceMAC;
    private List<BluetoothDevice> BTdevice = new ArrayList<BluetoothDevice>();
    private boolean mScanning;
   // private Handler mmlehandler = new Handler();
    private boolean scan_flag;
    int REQUEST_ENABLE_BT = 1;
    //private ArrayList<Integer> rssis;

    // 蓝牙扫描时间
    private static final long SCAN_PERIOD = 2000;
    private Button btn,btn1;

//    public static int bloodInt=0;
    
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
 //   private String mDeviceName;
 //   private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    byte[] WriteBytes = new byte[20];
    private BroadcastReceiver dataReceiver;
   // ThreadPool threadPoolble = new ThreadPool(5);

    public  ExecutorService blecachedThreadPool = Executors.newCachedThreadPool();
    public  ScheduledExecutorService blescheduledExecutorService = Executors.newScheduledThreadPool(3);

    private TextView height_rate_data,low_rate_data,heart_rate_data,xueyadongtai,fanhuianniu,zhuangtaixianshiView;
    private  String RpointString = "", QpointString = "", SpointString = "", Q1pointString = "", S2pointString = "", TpointString = "", PpointString = "";
    private final static String TAG = bloodactivity.class.getSimpleName();
    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是蓝牙模块的UUID
    public static String HEART_RATE_MEASUREMENT = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String EXTRAS_DEVICE_RSSI = "RSSI";
    //蓝牙连接状态
    private boolean mConnected = false;
    private String status = "disconnected";

    //private Toolbar letoolbar;
    private ProgressDialog pd;
    public static Handler mleHandler;   //信息处理机
    public static int LECONNECT_STATE = 0;      //标示当前连接状态
    public static final int LECONNECTING = 1;       //正在连接
    public static final int LECONNECT_SUCCESS = 2;//连接成功
    public static final int LECONNECT_BREAK = 3;    //连接断开
    public static final int HATA1 =11;
    public static final int HATA23 =12;
    public static final int HATA12 =13;    
    public static final int HATA3 =14; 
    public static final int HATA123 =15; 
    
    private TextView  connectState;
    private Context context;

    //蓝牙名字
    public String mDeviceName;
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    //蓝牙地址
    public String mDeviceAddress;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
   private static BluetoothGattCharacteristic target_chara = null;
    SQLiteHelp uService = new SQLiteHelp(bloodactivity.this);
   
    private TextView gaoyatxt, diyatxt, xinlvtxt, valuetxtTextView, timeTextView,
    powertxt;
    private String xueyaValue,wenduval;
    private Button startceliangBtn, saveButton, unsaveButton,celiangButton,fanhuiButton,chaxunanniuButton,baocunanniuButton,bangzhuxueyaButton,lishiqushiButton;
    private String s1, xygao, xydi, xyheart, s1andy = "", s3andy = "";
    private ImageView backImageView,lishiImageView,bangzhuImageView,baocunImageView,fanhuiImageView,celiangImageView;
    private OutputStream output = null;
    private boolean firststart = true;
    private View view1,viewbig;
    boolean bRun = true;
    boolean bThread = false;
    boolean scanfirst = true;
    private InputStream input = null;
    public static boolean mflagBl;
    public static String NAMEBl;  
    public static boolean jieshuflag=false;
    public static boolean xueyaceliangkaishi=false;
    public static boolean wenduchuanzhi=false;
    public static boolean sender1=false;
 //   public static boolean sender2=false;
    public static boolean xindian;
    public static   boolean onqidong = false;
    public static   boolean onqidong1 = false;
    public static   boolean xindianqidong = false;
    public static boolean chushihuaxueya=false;
   
    public static boolean firstSaveBoolean = false;
    
  SQLiteHelp sqlservice = new SQLiteHelp(bloodactivity.this);
    
    private int screenWidth,screenHeight;
    // handler 处理常量


    
    protected void onCreate(Bundle savedInstanceState) {

   /*     LayoutInflater inflater = this.getLayoutInflater();
        finddevice = inflater.inflate(R.layout.btconnect, null);
        check = inflater.inflate(R.layout.check, null);
        setContentView(check);  
*/
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = this.getLayoutInflater();
        view1 = inflater.inflate(R.layout.bloodpress, null);
        setContentView(R.layout.bloodpress);  
  
        initView();
        myCircleView.setMinDegree(10);
        myCircleView.setMaxDegree(10);
        myCircleView.setCurrentDegree(10);
        myCircleView.setGetDegreeInterface(this);
       
     //   setContentView(R.layout.xueyalishijilu);
        initViews();
        setTabSelection(0, DAY);
        
      init_ble();
      scan_flag = true;
      scanLeDevice(true);
      chushihuaxueya=true;
      Intent intent = getIntent();
      NAMEBl = intent.getStringExtra("username");
      
     
      
      connectState = (TextView)this.findViewById(R.id.lanyalianjie1);
     
          connectState.setText("蓝牙未连接");
    
      
      xindian=true;
       context = bloodactivity.this;
 
     //   init();
    
     //   height_rate_data=(TextView) findViewById(R.id.wendu);
  
    //  lishiImageView = (ImageView)findViewById(R.id.lishijilu123);
    //    lishiImageView.setOnClickListener(this);
     //   baocunImageView = (ImageView)findViewById(R.id.baocun123);
     //   baocunImageView.setOnClickListener(this);
       
        fanhuiImageView = (ImageView)findViewById(R.id.fanhui123);
        fanhuiImageView.setOnClickListener(this);

        myCircleView.setMinDegree(10);
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (MainActivity.class) {
 
                    for (int i = 10; i <101; i++) {//因为270/3=90
                       try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                        myCircleView.setMaxDegree(i);
//                      j++;
//                      if (j<83) {//假如当前温度是34度   （34-10）*3  =72
//                          myCircleView.setCurrentDegree(j);
//                      }
                        myCircleView.postInvalidate();
                    }
           
                }
            }
        }).start();
        mleHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case LECONNECT_SUCCESS:
                     
                          LECONNECT_STATE = LECONNECT_SUCCESS;

                          //letoolbar.setTitle("连接成功");
                          connectState.setText("蓝牙已连接");
                          onqidong=true;
                          jieshuflag=true;
                          break;
                   
                       
                    case LECONNECTING:
                        LECONNECT_STATE = LECONNECTING;

                        //letoolbar.setTitle("正在连接...");
                        connectState.setText("正在连接...");
                        break;
                    case LECONNECT_BREAK:
                        LECONNECT_STATE = LECONNECT_BREAK;
                      //  letoolbar.setTitle("连接断开");
                        if (bdlishiactivity.fanhuixy!=false) {
                            connectState.setText("蓝牙已连接"); 
                            bdlishiactivity.fanhuixy=false;
                        }else if (bloodknowledge.fanhuixy2!=false) {
                            connectState.setText("蓝牙已连接");
                            bloodknowledge.fanhuixy2=false;
                        }else {
                            connectState.setText("连接断开");
                        }
                     
                        
                        //mBLEservice.close();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        mScanning = true;
                        scan_flag = false;
                        
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                        
                        break;
                  
                    case HATA1:
                        String hata1 = msg.obj+"";
                        Log.e("ggggggfff",hata1);
                    //    float wd=Float.parseFloat(hata1.substring(0, 4));
                     //   Log.e("fffffffffffff",wd+"");
                     //  height_rate_data.setText(hata1);
                     
                        break;
                  
                }
            }
        };
      //  getInstance(this).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }
    private void initViews() {
        format = new SimpleDateFormat("yyyyMMddHHmmss");
        format2 = new SimpleDateFormat("yyyyMMdd");
        TextA = (TextView) findViewById(R.id.ri_text0);
        TextB = (TextView) findViewById(R.id.zhou_text0);
        TextC = (TextView) findViewById(R.id.yue_text0);
        TextD = (TextView) findViewById(R.id.ji_text0);
        linear0 = (RelativeLayout) findViewById(R.id.linear00);
      

        TextA.setOnClickListener(this);
        TextB.setOnClickListener(this);
        TextC.setOnClickListener(this);
        TextD.setOnClickListener(this);
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

         case 3:
                TextD.setTextColor(color.qianlvse);
                TextD.setBackground(getResources().getDrawable(R.drawable.whilt9));
                RefreshListData(3);
                break;
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
       case 3:
                
                cl.setTime(dateNow);
                
                cl.add(Calendar.DAY_OF_MONTH, -7); // 一季
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
           //     Log.e("ssssssssssssssssss", wendu+"");
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
        TextD.setTextColor(Color.WHITE);
        TextA.setBackground(getResources().getDrawable(R.drawable.txt123));
        TextB.setBackground(getResources().getDrawable(R.drawable.txt123));
        TextC.setBackground(getResources().getDrawable(R.drawable.txt123));
        TextD.setBackground(getResources().getDrawable(R.drawable.txt123));

    }
    private void initView() {
        myCircleView=(MyCircleView) findViewById(R.id.myCircleView);
       // actualDegree=(TextView) findViewById(R.id.actul_degree);
      //  clearBt=(Button) findViewById(R.id.clear);
 
        
    }
    @Override
    public void getActualDegree(float degree) {
        // TODO 自动生成的方法存根
        actualDegree.setText("当前温度："+degree+" ℃");
    }
    

    /**
     * 
     * @param minDegree  最低温度
     * @param maxDegree  最高温度
     * @param currentDegree 当前温度
     */
    private void showDegree(final int minDegree, final int maxDegree, final int currentDegree) {
        myCircleView.setMinDegree(minDegree);
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                for (int i = minDegree; i < (maxDegree-minDegree)*3+minDegree; i++) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                    
                    myCircleView.setMaxDegree(i);
                    myCircleView.postInvalidate();
                }
                for (int j = 10; j<=(currentDegree-10)*3+10; j++) {
                    if (j<=(currentDegree-10)*3+10) {//23*3+10=79
                        myCircleView.setCurrentDegree(j);
                    }
                    myCircleView.postInvalidate();
                }
            }
        }).start();
        
//      new Thread(new Runnable() {
//          
//          @Override
//          public void run() {
//              for (int j = 10; j<=(currentDegree-10)*3+10; j++) {
//                  if (j<=(currentDegree-10)*3+10) {//23*3+10=79
//                      myCircleView.setCurrentDegree(j);
//                  }
//                  myCircleView.postInvalidate();
//              }
//          }
//      }).start();
    }


    
    private void displayData(float data) {

      
         
 
          final float data1=data;
          final int sss=(int)data1;
           Log.e("<<<<<<<<<<<<<<floattt>>>>>>>>>>>>>>",data1+""); 
         //  Log.e("<<<<<<<<<<<<<<inttttt>>>>>>>>>>>>>>",sss+""); 
       //   height_rate_data.setText(data1);
          
   
            myCircleView.setMinDegree(10);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (MainActivity.class) {
                        float currentDegree=data1;
                        Log.e("ssssssss", currentDegree+"");
                        for (int i = 10; i <101; i++) {//因为270/3=90
   /*                        try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            }*/
                            myCircleView.setMaxDegree(i);
//                          j++;
//                          if (j<83) {//假如当前温度是34度   （34-10）*3  =72
//                              myCircleView.setCurrentDegree(j);
//                          }
                            myCircleView.postInvalidate();
                        }
                        for (int j = 10; j<=(currentDegree-10)*3+10; j++) {
                            if (j<=(currentDegree-10)*3+10) {//23*3+10=79
                                myCircleView.setCurrentDegree((currentDegree-10)*3+10);
                            }
                            myCircleView.postInvalidate();
                        }
                      /*  for (int i = 20; i < 101; i++) {//因为270/3=90
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            }
                            myCircleView.setMaxDegree(i);
//                          j++;
//                          if (j<83) {//假如当前温度是34度   （34-10）*3  =72
//                              myCircleView.setCurrentDegree(j);
//                          }
                            myCircleView.postInvalidate();
                        }*/
                    }
                }
            }).start();
            MyCircleView.wendujieshu=true;
            try {
                if (onqidong){
                if (firstSaveBoolean!=true)
                {
                    String gaoStringv =String.valueOf(BluetoothLeService.tt);
                    //    String diStringv = low_rate_data.getText().toString().trim();
                    //    String xinlvStringv =heart_rate_data.getText().toString().trim();

                        sqlservice.insert_Bloodpre1(NAMEBl, CurrentDate1(),gaoStringv);
                        Log.e("77777777", gaoStringv.substring(0, 4)); 
                        firstSaveBoolean = true;
                        DisplayToast("保存完成");
                        Log.e("444443334444444", "3222222222");
                    }else {
                        DisplayToast("不要再点我了，您刚刚保存过了...");
                     //   Log.e("77777777", gaoStringv.substring(0, 4));
                    }
                
                }else {
                    DisplayToast("未检测到有效温度值");
                    Log.e("6666666", "8888888888888");
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                DisplayToast("保存失败");
            }
       
      
       
    }     
         
      
   
    private void displayData2(String data) {
       
        if (data != null) {   
         String data1=data;
      
      Log.e("<<<<<<<<<<<<<<shu33>>>>>>>>>>>>>>",data1);  
    
     //   height_rate_data.setText(data1); 
      //   Log.e("524455444221", "mmmmmmmmmm");
         
  /*       Intent intent = new Intent();
         intent.setClass(bloodactivity.this, MyCircleView.class);
         intent.putExtra("data", data1);
         startActivity(intent); 
         */
 /*        List<String> fl = new ArrayList<String>();    // 泛型，这ArrayList只能放Float类型
         fl.add(data1);
         String[] f = new String[fl.size()];
         for (int s = 0; s < fl.size(); s++) {
             f[s] = fl.get(s);
        //e("LOL1124556", f[0]);
           Log.e("LOL1124556", f[0]);
           Log.e("LOL1124556", f[1]);
         }*/
    /*     if (BluetoothLeService.celiangcuowuzt1) {
             chaxunzhuangtai(); 
             Log.e("524455444221", "mmmmmmmmmm");
      } 
    */
         List<String> list = new ArrayList<String>();
         list.add(data1);
        // List<ArrayList<String>> dataListt = new ArrayList<ArrayList<String>>();
         
     //    dataListt.add(data1);
        }
      
    }
   
 
  
    private void displayData8(String data) {

        if (data != null) {

            String data1=data;
           Log.e("<<<<<<<<<<<<<<uuuuuu>>>>>>>>>>>>>>",data1); 
           zhuangtaixianshiView.setText(data1);     
             
        }
    }  
    @SuppressLint("NewApi")
    private void init_ble()
    {
        // TODO Auto-generated method stub
        // 手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
  
    @SuppressLint("NewApi")
    private void scanLeDevice( boolean enable) {
        if (enable) {
            /*mleHandler.postDelayed(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    mScanning = false;
                    scan_flag = true;
                    Log.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);*/
            Log.i("SCAN", "begin.....................");
            mScanning = true;
            scan_flag = false;

            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.i("SCAN1111", "after........begin.....................");
        } else
        {
            Log.i("Stop", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
        }

    }
    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @SuppressLint("NewApi")
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord)
        {

            if (device.getName()!=null){
               
                if (device.getName().contains("FSRKB-EWQ01")){
                   
                    mDeviceName = device.getName().toString();
                    mDeviceAddress =device.getAddress().toString();
                       

                    if (mScanning)
                    {
                        //停止扫描设备
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        scan_flag = true;
                    }
                    if (LECONNECT_STATE!=LECONNECT_SUCCESS){
                    /* 启动蓝牙service */
                        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
                        getApplicationContext().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    }
                }
            }else {
              //  Log.e("vvvvvvvvvvvvvv","device is null....");
            }


        }
    };

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service)
        {
            mBluetoothLeService = (( BluetoothLeService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize())
            {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            mBluetoothLeService = null;
        }

    };
    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt连接成功
            {
                mConnected = true;
                status = "connected";
                //更新连接状态
              //  updateConnectionState(status);
                System.out.println("BroadcastReceiver :" + "device connected");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED//Gatt连接失败
                    .equals(action))
            {
                mConnected = false;
                status = "disconnected";
                //更新连接状态
                //updateConnectionState(status);
                System.out.println("BroadcastReceiver :"
                        + "device disconnected");

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED//发现GATT服务器
                    .equals(action))
            {
                // Show all the supported services and characteristics on the
                // user interface.
                //获取设备的所有蓝牙服务
                displayGattServices(mBluetoothLeService
                        .getSupportedGattServices());
                System.out.println("BroadcastReceiver :"
                        + "device SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))//有效数据
            {
                //处理发送过来的数据
                displayData2(intent.getStringExtra(BluetoothLeService.EXTRA_DATA2));             
               displayData(intent.getFloatExtra(wenduval, BluetoothLeService.tt));
                displayData8(intent.getStringExtra(BluetoothLeService.EXTRA_DATA8)); 
               firstSaveBoolean=false;
               wenduchuanzhi=true;
              
            }
        }
    };
    
 
  
    @SuppressLint("NewApi")
    private void displayGattServices(List<BluetoothGattService> gattServices)
    {

        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices)
        {

            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

            gattServiceData.add(currentServiceData);

            System.out.println("Service uuid:" + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
            {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString()
                        .equals(HEART_RATE_MEASUREMENT))
                {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mleHandler.postDelayed(new Runnable()
                    {

                       
                        public void run()
                        {
                            // TODO Auto-generated method stub
                            mBluetoothLeService
                                    .readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(
                            gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors)
                {
                    System.out.println("---descriptor UUID:"
                            + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }

                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);

        }

    }

    
    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    



    // 蓝牙写入，将数据转换为16进制;
    private byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

   @Override
    protected void onDestroy() {
      if (jieshuflag==true) {
        super.onDestroy();
        BluetoothLeService.mBluetoothGatt.disconnect();
    }else {
        super.onDestroy();
    }
      
     //   mBluetoothLeService.close();
     //   unregisterReceiver(mGattUpdateReceiver);
  //  unbindService(mServiceConnection);
 //   mBluetoothLeService.close();
   // unregisterReceiver(mGattUpdateReceiver);
    //   mBluetoothLeService.close();
       
     // mBluetoothLeService = null;
         
    }
  


   


    @Override
    protected void onStop() {
        super.onStop();
       // mBLEservice.close();
    }
   
    private long CurrentDate1() {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        long time = Long.parseLong(format.format(date));
        return time;

    }
    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        // 设置toast显示的位�?
         toast.setGravity(Gravity.CENTER, 0, 220);
        // 显示该Toast
        toast.show();
    }



    // *返回键退出确认*/
   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK )
        {

            //Toast.makeText(MainActivity.this, "点击返回键", Toast.LENGTH_SHORT).show();
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }
            );
            isExit.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    wenduchuanzhi=false;
                    finish();
                    xindianqidong=true;
                }
            });
            isExit.show();
        }
       return super.onKeyDown(keyCode, event);
   //     return true;
    }
@Override
public void onClick(View view) {
   
    switch (view.getId()) {
      
   /*     case R.id.baocun123:
           
          
            String gaoStringv =String.valueOf(BluetoothLeService.tt);
            //    String diStringv = low_rate_data.getText().toString().trim();
            //    String xinlvStringv =heart_rate_data.getText().toString().trim();

                sqlservice.insert_Bloodpre1(NAMEBl, CurrentDate1(),gaoStringv);
                Log.e("77777777", gaoStringv.substring(0, 4)); 
                firstSaveBoolean = true;
                DisplayToast("保存完成");
                Log.e("444443334444444", "3222222222");  
                try {
                    if (onqidong){
                    if (firstSaveBoolean!=true)
                    {
                        String gaoStringv =String.valueOf(BluetoothLeService.tt);
                        //    String diStringv = low_rate_data.getText().toString().trim();
                        //    String xinlvStringv =heart_rate_data.getText().toString().trim();

                            sqlservice.insert_Bloodpre1(NAMEBl, CurrentDate1(),gaoStringv);
                            Log.e("77777777", gaoStringv.substring(0, 4)); 
                            firstSaveBoolean = true;
                         //   DisplayToast("保存完成");
                                      
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                                    Locale.getDefault());
                            String strd = sdf.format(new Date());   
                            LayoutInflater inflater = LayoutInflater.from(bloodactivity.this);
                            final View textEntryView = inflater.inflate(R.layout.wendulishi, null);
                            final String Time = strd;
                            final TextView resulttext0 = (TextView) textEntryView
                                    .findViewById(R.id.wendujieguo);                        
                            final List<String> Result = uService.wendu_sel(MainActivity.NAME, Time);
                            
                            float wendu=Float.parseFloat(Result.get(0));
                            if (wendu<35.7f) {
                         	   resulttext0.setText(Result.get(0)+"℃"+"   "+"偏低");
     				    	}else if (wendu>=35.7f&&wendu<=37.5f) {
     						   resulttext0.setText(Result.get(0)+"℃"+"   "+"正常");
     				    	}else if (wendu>37.5f) {
     						 resulttext0.setText(Result.get(0)+"℃"+"   "+"偏高");
     					    }      
         
                            final AlertDialog.Builder builder = new AlertDialog.Builder(bloodactivity.this);
                            builder.setCancelable(false);
                            builder.setIcon(R.drawable.ic_launcher);
                            builder.setTitle("温度值供参考");
                            builder.setView(textEntryView);
                            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(bloodactivity.this, "保存完成", Toast.LENGTH_SHORT).show();

                                }
                            });
                            builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    uService.deletevalue(MainActivity.NAME, Time);

                                    File path = new File(Environment.getExternalStorageDirectory() + "//"
                                            + "HWDAD"+"//" + Time + ".txt");
                                    if (path.exists()) {
                                        path.delete();

                                    }
                                    Toast.makeText(bloodactivity.this, "数据已经删除", Toast.LENGTH_SHORT).show();
                                    mleHandler.obtainMessage(bleactivty.SAVEDATA).sendToTarget();

                                }
                            });
                            builder.show();
                            
                            Log.e("444443334444444", "3222222222");
                        }else {
                            DisplayToast("不要再点我了，您刚刚保存过了...");
                         //   Log.e("77777777", gaoStringv.substring(0, 4));
                        }
                    
                    }else {
                        DisplayToast("未检测到有效温度值");
                        Log.e("6666666", "8888888888888");
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    DisplayToast("保存失败");
                }
          
            break;*/
        case R.id.fanhui123:
     /*      if (xueyaceliangkaishi==true) {
                sendorder();     
           }*/
           //  System.exit(0);
            wenduchuanzhi=false;
            finish();
            xindianqidong=true;
            break;
        case R.id.ri_text0:
            setTabSelection(0,DAY);
            break;
        case R.id.zhou_text0:
            setTabSelection(1, ZHOU);
            break;
        case R.id.yue_text0:
            setTabSelection(2, YUE);
            break;
        case R.id.ji_text0:
            setTabSelection(3, JI);
            break;    
        default:
            break;
    } 
}

}
