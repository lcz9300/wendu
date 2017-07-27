package com.HWDTEMPT.hwdtempt;

import android.Manifest;
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
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.HWDTEMPT.model.FindingPots;
import com.HWDTEMPT.model.SQLiteHelp;
import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.tool.Analyse;
import com.HWDTEMPT.tool.FindR;
import com.HWDTEMPT.tool.RawDataArray;
import com.HWDTEMPT.view.CircularSeekBar;
import com.huicheng.service.BluetoothLeService;

import java.io.File;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class bleactivty extends Activity implements OnClickListener{
    
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
    private Button btn,btn1,fanhuishouyeButton,fanhuijian;

    public static int gainInt=0;
    
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

   // ThreadPool threadPoolble = new ThreadPool(5);
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    //记录两个触屏点的坐标
    private int x1, x2, y1, y2;
    //倍率
    private float rate = 1;
    //记录上次的�?�?
    private float oldRate = 1;
    //记录第一次触屏时线段的长�?
    private float oldLineDistance;
    //判定是否头次多指触点屏幕
    private boolean isFirst = true;
    private int valuEvent=3;
    private MotionEvent event;

    public  ExecutorService blecachedThreadPool = Executors.newCachedThreadPool();
    public  ScheduledExecutorService blescheduledExecutorService = Executors.newScheduledThreadPool(3);

    private  String RpointString = "", QpointString = "", SpointString = "", Q1pointString = "", S2pointString = "", TpointString = "", PpointString = "";
    private final static String TAG = bleactivty.class.getSimpleName();
    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是蓝牙模块的UUID
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String EXTRAS_DEVICE_RSSI = "RSSI";
    //蓝牙连接状态
    private boolean mConnected = false;
    private String status = "disconnected";
    private int screenWidth,screenHeight;
    //private Toolbar letoolbar;
    private ProgressDialog pd;
    public static Handler mleHandler;   //信息处理机
    public static int LECONNECT_STATE = 0;      //标示当前连接状态
    public static final int LECONNECTING = 1;       //正在连接
    public static final int LECONNECT_SUCCESS = 2;//连接成功
    public static final int LECONNECT_BREAK = 3;    //连接断开
    public static final int TIMESTART = 4;  //开始计算
    public static final int DATAFAILE = 5;  //接收失败
    public static final int COMPUTFINISH = 6;//计算完毕
    public static final int SAVEDATA = 7;
    public static final int SS_HEART = 8;//实时心率
    public static final int WAVEUNSTABLE=9;
    public static final int BMDHEARTRATE =10;
    public static boolean jieshuflag=false;
    public static boolean ksjian=false;  
    public static boolean fenbianlvda=false;
    public static boolean fenbianlvxiao=false;
    public static boolean fenbianlvxiao2=false;
    public static boolean fenbianlvxiao52=false;
    public static boolean fenbianlvxiaoqita=false;
    public static boolean chushihuaxindian=false;
    private TextView  connectState;
  //  private View finddevice;
 //   private View check;
    
    private Context context;
    private FindR  FindRs;
    private FindingPots Findpots;// 分析算法
    private Bundle b;
    //蓝牙名字
    public String mDeviceName;
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    //蓝牙地址
    public String mDeviceAddress;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    SQLiteHelp uService = new SQLiteHelp(bleactivty.this);
    int values = 1;
    private CircularSeekBar seekbar;//倒计时控件
    private Boolean isstart = false;
    private Boolean isshishi =true;
    private TextView heartbeartreale, timestart;//心率，倒计时
    private ImageView backImageView;
    private List<Integer> ECGsignal = new ArrayList<Integer>();// 解析包数据得到的数据
    private List<Integer> ECGsignal_ss=new ArrayList<Integer>();
    private String ECG = "";// 用于保存txt数据的点
    Runnable runb = new Runnable() {
        @Override
        public void run() {
            seekbar.setProgress(values);
            seekbar.setMax(20);
            long remainingTime = 21 - values;
            timestart.setText(remainingTime / 60 + ":" + remainingTime % 60);
            if (values < 20) {
                mleHandler.postDelayed(this, 1000);
            } else {
                mleHandler.removeCallbacks(runb);
                // simpleread = false;
                isstart=false;
                mleHandler.obtainMessage(bleactivty.TIMESTART).sendToTarget();
            }
            values++;
        }

    };
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
   /*     LayoutInflater inflater = this.getLayoutInflater();
        finddevice = inflater.inflate(R.layout.btconnect, null);
        check = inflater.inflate(R.layout.check, null);
        setContentView(check);  
*/
        
       connectState = (TextView)this.findViewById(R.id.connectble);
     
           connectState.setText("蓝牙未连接");
   
        timestart = (TextView)this.findViewById(R.id.time);
        timestart.setOnClickListener(this);
        heartbeartreale = (TextView)this.findViewById(R.id.textView2);
        seekbar = (CircularSeekBar)this.findViewById(R.id.circularseekbar);
        backImageView = (ImageView)findViewById(R.id.bloodback_imageview_gohomeblood1);
        backImageView.setOnClickListener(this);
        fanhuijian = (Button)findViewById(R.id.xindianfanhuij);
        fanhuijian.setOnClickListener(this);

       //Resources res = getResources();
      // float fontSize = res.getDimension(R.dimen.font_size);
        
        DisplayMetrics  dm = new DisplayMetrics();    
        //取得窗口属性    
        getWindowManager().getDefaultDisplay().getMetrics(dm);    
           
        //窗口的宽度    
        screenWidth = dm.widthPixels;    
           
        //窗口高度    
        screenHeight = dm.heightPixels;   
        Log.d(TAG, "Screen width : " + screenWidth);   
        Log.d(TAG, "Screen height : " + screenHeight);   
        double x = Math.pow(screenWidth/ dm.xdpi, 2);  
        double y = Math.pow(screenHeight/ dm.ydpi, 2);  
        double screenInches = Math.sqrt(x + y);  
        Log.e("2333433","222222222");
       Log.d(TAG, "Screen inches : " + screenInches);   
       if (screenWidth > 1500) {
           fenbianlvda=true;
         }else if( screenWidth > 1400&&screenWidth<1500) {
             fenbianlvxiao=true;
         }else if( screenWidth > 1000&&screenWidth<1200) {
             fenbianlvxiao2=true;
         }else if (screenInches>5.16&&screenInches<5.24) {
            fenbianlvxiao52=true;
            Log.e("cccccc", "3365533");   
       }else {
        fenbianlvxiaoqita=true;
    }
       
   
      getinfo();      
        /* letoolbar = (Toolbar)findViewById(R.id.toolbar);
        letoolbar.setTitle("设备未连接");
        letoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));*/

        //初始化画图view
 /*
        btn = (Button)findViewById(R.id.jieshou);
    
        fanhuishouyeButton=(Button)findViewById(R.id.shouyefanhui);
        fanhuishouyeButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
     
                    Intent i = new Intent(bleactivty.this,MainActivity.class);startActivity(i);    
                    System.exit(0);
         
                    
            }
        });
        
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               
                sendorder();
            }
        });
  */      
    /*    
        btn1= (Button)findViewById(R.id.guanbi);
        btn1.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               
                shutt();
            }
        });
    */    
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        StaticValue.surfaceviewWidth = localDisplayMetrics.widthPixels;
        RawDataArray.initArray();
        Findpots = new FindingPots(StaticValue.Fs);

        FindRs = new FindR(500);

        context = bleactivty.this;

        init_ble();
        scan_flag = true;
        scanLeDevice(true);


     /*   b = getIntent().getExtras();
        //从意图获取显示的蓝牙信息
        mDeviceName = b.getString(EXTRAS_DEVICE_NAME);
        mDeviceAddress = b.getString(EXTRAS_DEVICE_ADDRESS);*/

       // pd = ProgressDialog.show(Ble_Activity.this,"连接","正在连接,请稍等...");

        mleHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case LECONNECT_SUCCESS:
                       
                            LECONNECT_STATE = LECONNECT_SUCCESS;

                            //letoolbar.setTitle("连接成功");
                            connectState.setText("蓝牙已连接");
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
                        connectState.setText("连接断开");
                        
                        //mBLEservice.close();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        mScanning = true;
                        scan_flag = false;
                        
                 //       mBluetoothAdapter.startLeScan(mLeScanCallback);
                        
                        break;
                    case DATAFAILE:
                       // letoolbar.setTitle("接收失败");
                        connectState.setText("接收失败");
                        break;
                    case COMPUTFINISH:
                        pd.dismiss();
                        complate(msg.arg1+"",msg.obj+"");
                        break;
                    case SAVEDATA:
                        ECG = "";
                        ECGsignal.clear();
                        values = 1;
                        timestart.setText("开始");
                        RpointString = "";
                        QpointString = "";
                        SpointString = "";
                        Q1pointString = "";
                        S2pointString = "";
                        TpointString = "";
                        PpointString = "";
                        break;
                    case SS_HEART:

                        String ratepp = msg.obj+"";
                        Log.e("ss-heart",ratepp);
                        heartbeartreale.setText(ratepp);
                        //ECGsignal_ss.clear();

                        break;
                    case WAVEUNSTABLE:
                        Toast.makeText(context,"信号紊乱，保持静态测量姿势",Toast.LENGTH_SHORT).show();
                        break;
                    case BMDHEARTRATE:
                        String bmdrate = msg.obj+"";
                        Log.e("ss-heart",bmdrate);
                        heartbeartreale.setText(bmdrate);
                        break;
                    case TIMESTART:
                        pd = ProgressDialog.show(bleactivty.this,"分析","分析中,请稍等...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    comput();
                                }catch (Exception e){
                                    Toast.makeText(context,"检测出错，请重新检测",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).start();

                        break;
                }
            }
        };
      
        spinner = (Spinner)findViewById(R.id.spinner);//12.9 spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.Gain, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gainInt=Integer.parseInt(parent.getItemAtPosition(position).toString());
                Analyse.putgn(String.valueOf(gainInt), "gn");
                
                RawDataArray.getint(gainInt);
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" mm/mV 增益已选择",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

  /* blescheduledExecutorService.scheduleAtFixedRate(new Runnable() {
       @Override
       public void run() {

           try {
               ss_comput();
           }catch (Exception e){
               e.printStackTrace();
               Log.e("chucuoouuooo","111111111111111111111111111111111111111111");
           }

       }
   },6,5, TimeUnit.SECONDS);*/

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
               
                if (device.getName().contains("HWD002")){
                   
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
                    // 启动蓝牙service 
                        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
                        getApplicationContext().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    }
                }
            }else {
                Log.e("vvvvvvvvvvvvvv","device is null....");
            }


        }
    };
    

    

    // BluetoothLeService绑定的回调函数 
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
                //updateConnectionState(status);
                
                
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
                     int data = intent.getIntExtra(BluetoothLeService.EXTRA_DATA,0);
                     
                if (isstart)
                {
                    ECGsignal.add(data);
                }
    

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

    
    public void sendorder(){
        byte[] bb = new byte[6];
        bb[0]=(byte)0xaa;
        bb[1]=(byte)0x02;
        bb[2]=(byte)0x01;
        bb[3]=(byte)0x01;
        bb[4]=(byte)0xae;
        bb[5]=(byte)0xff;
        bleactivty.target_chara.setValue(bb);//只能一次发送20字节，所以这里要分包发送
        //调用蓝牙服务的写特征值方法实现发送数据
        mBluetoothLeService.writeCharacteristic(bleactivty.target_chara);
      
    }
    public void shutt(){
        byte[] bb = new byte[6];
        bb[0]=(byte)0xaa;
        bb[1]=(byte)0x02;
        bb[2]=(byte)0x01;
        bb[3]=(byte)0x02;
        bb[4]=(byte)0xaf;
        bb[5]=(byte)0xff;
        bleactivty.target_chara.setValue(bb);//只能一次发送20字节，所以这里要分包发送
        //调用蓝牙服务的写特征值方法实现发送数据
        mBluetoothLeService.writeCharacteristic(bleactivty.target_chara);
      
    }

 
    public void onClick(View view) {
    
        switch (view.getId()) {
            case R.id.time:
           
                if (!isstart) {
                    
                    if (LECONNECT_STATE==LECONNECT_SUCCESS) {
                        isstart=true;
                        mleHandler.post(runb);
                    }else {
                        Toast.makeText(bleactivty.this, "蓝牙未连接", Toast.LENGTH_LONG).show();
                    }
                   

                     
                  
                }
                break;
            case R.id.bloodback_imageview_gohomeblood1:
        
                //unregisterReceiver(mGattUpdateReceiver);
               // mBluetoothLeService = null;
               // mBluetoothLeService.close();
             //   System.exit(0);
                ChoiceHome.xindiankai1=false;
                finish();
                break;
            case R.id.xindianfanhuij:
               
                ChoiceHome.xindiankai1=false;
                //unregisterReceiver(mGattUpdateReceiver);
               // mBluetoothLeService = null;
               // mBluetoothLeService.close();
          //      System.exit(0);
                finish();
                break;
            default:
                break;
        }
      
        
    }
    
    

  
    private  void  ss_comput(){
        isshishi = false;

        int[] ECGsingal=new int[ECGsignal_ss.size()];

        for(int i=0;i<ECGsingal.length;i++)
        {
            ECGsingal[i]= ECGsignal_ss.get(i)*1000;
        }

        ECGsignal_ss.clear();
        isshishi = true;


        int[] Rpoint = FindRs.Find_Rpoints(ECGsingal);


        int rate1=0;

        if(Rpoint.length>1)
        {
            rate1 = Rpoint[Rpoint.length-1] - Rpoint[0];
           
            rate1 = rate1 /(Rpoint.length-1);
           
            rate1 = (60 * ((ECGsingal.length-1) / 5)) / rate1;
           


            mleHandler.obtainMessage(bleactivty.SS_HEART,-1,-1,rate1).sendToTarget();
        }


    }
    private void comput() {

        int[] ECGsingal = new int[ECGsignal.size()];

        for (int i = 0; i < ECGsingal.length; i++)
        {
            ECGsingal[i] = ECGsignal.get(i) * 1000;
        }

        ECGsignal.clear();

        com.HWDTEMPT.model.WaveLet wave = new com.HWDTEMPT.model.WaveLet();
        Map<String, Object> map = wave.Wavedec(ECGsingal, 4, "db6");
        List<Integer> Ac = (List<Integer>) map.get("list1");

        int[] al = (int[]) map.get("list2");
        for (int i = al[0] + al[1]; i < Ac.size(); i++) {
            Ac.set(i, 0);
        }
        ECGsingal = wave.appcoef(Ac, al);
        ECGsingal = Findpots.Middle_Filter(ECGsingal);
        Log.e("tt", "数据转换完成");

        // 数据转换完成之后，去掉前面的半秒，后面的半秒，这是为了找点准�?

        int[] ECGreal = new int[ECGsingal.length - StaticValue.Fs];

        System.arraycopy(ECGsingal, StaticValue.Fs / 2 - 1, ECGreal, 0,
                ECGreal.length);

        for (int i = 0; i < ECGreal.length; i++) {
            ECG = ECG + ECGreal[i] + ",";

        }
        Log.e("ecg",ECG);
        int[] Rpoint = Findpots.Find_Rpoints(ECGreal);

        for (int i = 0; i < Rpoint.length; i++)
        {
            RpointString += Rpoint[i] + ",";
        }

        int RRrate = 0;
        if (Rpoint.length > 1)
        {
            RRrate = Rpoint[Rpoint.length - 1] - Rpoint[0];
            RRrate = RRrate / (Rpoint.length - 1);
            RRrate = (60 * ((ECGsingal.length-1) / 19)) / RRrate;

        }

        int[] Qpoint = Findpots.Finding_Qpoints(ECGreal, Rpoint);

        for (int i = 0; i < Qpoint.length; i++)
        {
            QpointString += Qpoint[i] + ",";
        }

        int[] Spoint = Findpots.Finding_Spoints(ECGreal, Rpoint);

        for (int i = 0; i < Spoint.length; i++)
        {
            SpointString += Spoint[i] + ",";
        }

        int[] Q1point = Findpots.Finding_Q1points(ECGreal, Qpoint);

        for (int i = 0; i < Q1point.length; i++)
        {
            Q1pointString += Q1point[i] + ",";
        }

        int[] S2point = Findpots.Finding_S2points(ECGreal, Spoint);

        for (int i = 0; i < S2point.length; i++)
        {
            S2pointString += S2point[i] + ",";
        }

        int[] Tpoint = Findpots.Finding_Tpoints(ECGreal, S2point);

        for (int i = 0; i < Tpoint.length; i++)
        {
            TpointString += Tpoint[i] + ",";
        }

        int[] Ppoint = Findpots.Findg_Ppoints(ECGreal, Q1point);

        for (int i = 0; i < Ppoint.length; i++)
        {
            PpointString += Ppoint[i] + ",";
        }

        int[] resultdouble = Findpots.Find_Parameters_and_Result(ECGreal,
                Rpoint, Q1point, S2point, Ppoint, Tpoint);

        Log.e("", "jieshujishuan");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault());
        String strd = sdf.format(new Date());
        Log.e("TIME",strd);
        Analyse.put(ECG, strd);
        
        Analyse.putgnhis(String.valueOf(gainInt), "gain"+strd);
        //put(ECG, strd);


        uService.insert_Diagnosis(MainActivity.NAME, Long.parseLong(strd),
                RpointString, QpointString, Q1pointString, SpointString,
                S2pointString, PpointString, TpointString, RRrate);
        uService.insert_Result(MainActivity.NAME, Long.parseLong(strd), RRrate,
                resultdouble[0], resultdouble[1], resultdouble[2],
                resultdouble[3], resultdouble[4], resultdouble[5],
                resultdouble[6], resultdouble[7], resultdouble[8],
                resultdouble[9]);


        mleHandler.obtainMessage(bleactivty.COMPUTFINISH,RRrate,-1,strd).sendToTarget();
       // Log.i(MainActivity.TAG,"COMPUTFINISH");

    }
    private void complate(String rate,String time){
        
        LayoutInflater inflater = LayoutInflater.from(bleactivty.this);
        final View textEntryView = inflater.inflate(R.layout.result_layout, null);
        final TextView heartbeat = (TextView) textEntryView
                .findViewById(R.id.textView1);
        final String Heartbeat = rate;
        final String Time = time;
        heartbeat.setText(Heartbeat + "次/分钟");
        final TextView resulttext0 = (TextView) textEntryView
                .findViewById(R.id.textView_xueyaVal);
        final TextView resulttext1 = (TextView) textEntryView
                .findViewById(R.id.textView3);
     /*  final TextView resulttext2 = (TextView) textEntryView
                .findViewById(R.id.textView5);
        final TextView resulttext3 = (TextView) textEntryView
                .findViewById(R.id.textView7);
       final TextView resulttext4 = (TextView) textEntryView
                .findViewById(R.id.textView9);
        final TextView resulttext5 = (TextView) textEntryView
                .findViewById(R.id.textView11);
        final TextView resulttext6 = (TextView) textEntryView
                .findViewById(R.id.textView13);
        final TextView resulttext7 = (TextView) textEntryView
                .findViewById(R.id.textView15);
       final TextView resulttext8 = (TextView) textEntryView
                .findViewById(R.id.textView17);
        final TextView resulttext9 = (TextView) textEntryView
                .findViewById(R.id.textView19);
        final TextView resulttext10 = (TextView) textEntryView
                .findViewById(R.id.textView21);

       final TextView resulttext11 = (TextView) textEntryView
                .findViewById(R.id.textView4);
       final TextView resulttext12 = (TextView) textEntryView
                .findViewById(R.id.textView6);
        final TextView resulttext13 = (TextView) textEntryView
                .findViewById(R.id.textView8);
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

        final List<String> Result = uService.FindfromSQLite(MainActivity.NAME, Time);
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
        resulttext20.setText(returnResult(Result.get(9)));

*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(bleactivty.this);
        builder.setCancelable(false);

        builder.setTitle("检测结果仅供参考");
        builder.setView(textEntryView);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(bleactivty.this, "保存完成", Toast.LENGTH_SHORT).show();

                mleHandler.obtainMessage(bleactivty.SAVEDATA).sendToTarget();
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
                Toast.makeText(bleactivty.this, "数据已经删除", Toast.LENGTH_SHORT).show();
                mleHandler.obtainMessage(bleactivty.SAVEDATA).sendToTarget();

            }
        });
        builder.show();

    }
    private String returnResult(String rate)
    {
        String result = "";
        int number = Integer.parseInt(rate);
        if (number < 50)
        {
            result = "正常";
        } else
        {
            result = "偏高";
        }
        return result;
    }
    @Override

    protected void onDestroy()
    {
     
          if (jieshuflag==true) {
              super.onDestroy();
              // unregisterReceiver(mGattUpdateReceiver);
               //unbindService(mServiceConnection);
         
             BluetoothLeService.mBluetoothGatt.disconnect();
        }else {
            super.onDestroy();
        }   
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume()
    {
        super.onResume();
        //绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null)
        {
            //根据蓝牙地址，建立连接
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
           Log.d(TAG, "Connect request result=" + result);
        }
    }
  
    @Override
    protected void onPause() {
  /*      super.onPause();
        scanLeDevice(false);

      mBLEservice.disconnect();
      */
      super.onPause();
      unregisterReceiver(mGattUpdateReceiver);
    }


    @Override
    protected void onStop() {
        super.onStop();
    //   mBLEservice.close();
    }
   
   
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_UP){
            isFirst = true;
            oldRate = rate;
        }else {
            if (event.getPointerCount()>1){
                x1 = (int)event.getX(0);
                y1 = (int)event.getY(0);
                x2 = (int)event.getX(1);
                y2 = (int)event.getY(1);
                if (event.getPointerCount() ==2){
                    if (isFirst){
                        //得到第一次触屏时线段的长�?
                        oldLineDistance = (float) Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2));
                        isFirst = false;
                       // Log.e(MainActivity.TAG,""+oldLineDistance);

                    }else {
                        //得到非第一次触屏时线段的长度
                        float newLineDistance = (float) Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2));
                        //获取本次的缩放比
                        rate = oldRate * newLineDistance / oldLineDistance;
                       /* Log.e(MainActivity.TAG,"rate是"+rate);
                        Log.e(MainActivity.TAG,"newLineDistance"+newLineDistance);*/
                        if (newLineDistance<oldLineDistance){

                           // RawDataArray.Surface_rate(100);
                        }else {
                            //RawDataArray.Surface_rate(250);
                        }


                    }
                }
            }
        }

        return true;
    }

     private void getinfo(){
    
  
         DisplayMetrics dm = getResources().getDisplayMetrics();  
         screenWidth = dm.widthPixels;    
         
         //窗口高度    
         screenHeight = dm.heightPixels;   
         Log.d(TAG, "Screen width : " + screenWidth);   
         Log.d(TAG, "Screen height : " + screenHeight);   
         double x = Math.pow(screenWidth/ dm.xdpi, 2);  
         double y = Math.pow(screenHeight/ dm.ydpi, 2);  
         double screenInches = Math.sqrt(x + y);  
         Log.e("2333433","222222222");
        Log.d(TAG, "Screen inches : " + screenInches);   
         if (screenInches>5.16&&screenInches<5.24) {
             fenbianlvxiao52=true;
             Log.e("cccccc", "3365533");   
        }
   /*
         Point point = new Point();  
         getWindowManager().getDefaultDisplay().getRealSize(point);  
         DisplayMetrics dm = getResources().getDisplayMetrics();  
         double x = Math.pow(point.x/ dm.xdpi, 2);  
         double y = Math.pow(point.y / dm.ydpi, 2);  
         double screenInches = Math.sqrt(x + y);  
         Log.d(TAG, "Screen inches : " + screenInches);  
  */       
       String xinghao = "SM-N9109W";
    TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);  
    String imei = mTm.getDeviceId();  
    String imsi = mTm.getSubscriberId();  
    String mtype = android.os.Build.MODEL; // 手机型号   
//   String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得  
    Log.e("2333433", mtype);           
    if (mtype.equals(xinghao)) {
        Log.e("44444444", mtype);   
    }


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

                    ChoiceHome.xindiankai1=false;
            /*      if (jieshuflag==true) {
                    shutt(); 
                }
                   */
                //  bleactivty.this.finish();
                  /*  Intent i = new Intent(bleactivty.this,MainActivity.class);startActivity(i);    
                    System.exit(0);*/
               //   System.exit(0);
                  finish();
                }
            });
            isExit.show();
        }
       return super.onKeyDown(keyCode, event);
   //     return true;
    }

}