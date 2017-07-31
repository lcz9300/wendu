/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huicheng.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.HWDTEMPT.hwdtempt.R.string;
import com.HWDTEMPT.hwdtempt.ChoiceHome;
import com.HWDTEMPT.hwdtempt.bleactivty;
import com.HWDTEMPT.hwdtempt.bloodactivity;
import com.HWDTEMPT.hwdtempt.bloodactivity;
import com.HWDTEMPT.tool.RawDataArray;
import com.HWDTEMPT.view.MyCircleView;
import com.HWDTEMPT.view.MySurfaceView;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class BluetoothLeService extends Service {

    //byte[] datar ;
    byte[] datarec;
    List<Byte> datarlist = new ArrayList<Byte>();
    private int position = 0;

   // private Runnable runnableble;

    int gint = bleactivty.gainInt;
   // ThreadPool threadPool = new ThreadPool(10);
    private final static String TAG = "BluetoothLeService";// luetoothLeService.class.getSimpleName();
    private List<Sensor> mEnabledSensors = new ArrayList<Sensor>();
    private static final int BUFSIZ = 60;
    private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUFSIZ);
    private byte EE = (byte) 0xEE; // 开始位
    private byte FF = (byte) 0xFF; // 结束位
    private byte KK = (byte) 0x99; // 开始位
    private byte AA = (byte) 0xAA; // 开始位
    int dataInt;
    //蓝牙相关类
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    public static BluetoothGatt mBluetoothGatt;
    public static final UUID SERVIE_UUID = UUID
            .fromString("00001910-0000-1000-8000-00805f9b34fb");
   // private int mConnectionState = Ble_Activity.LECONNECT_BREAK;
    public final static String ACTION_GATT_CONNECTED = "com.hc_ble.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.hc_ble.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.hc_ble.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.hc_ble.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.hc_ble.bluetooth.le.EXTRA_DATA";
    public final static String EXTRA_DATA2 = "com.hc_ble.bluetooth.le.EXTRA_DATA1";
    public final static String EXTRA_DATA3 = "com.hc_ble.bluetooth.le.EXTRA_DATA2";
    public final static String EXTRA_DATA4 = "com.hc_ble.bluetooth.le.EXTRA_DATA3";
    public final static String EXTRA_DATA5 = "com.hc_ble.bluetooth.le.EXTRA_DATA4";
    public final static String EXTRA_DATA6 = "com.hc_ble.bluetooth.le.EXTRA_DATA5";
    public final static String EXTRA_DATA7 = "com.hc_ble.bluetooth.le.EXTRA_DATA6";
    public final static String EXTRA_DATA8 = "com.hc_ble.bluetooth.le.EXTRA_DATA7";
    public static boolean jieshuflag11=false;
    public static boolean celiangcuowuzt=false;
    public static boolean celiangcuowuzt1=false;
    public static boolean xindianlanyakai=false;
    public static boolean xueyalanyakai=false;
    public static boolean layaduankai=false;
    public static float tt=0;
    // public final static UUID UUID_HEART_RATE_MEASUREMENT =zzzzzzzzzzzzz
    // UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    private OnDataAvailableListener mOnDataAvailableListener;

    public interface OnDataAvailableListener {

        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status);

        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic);

        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic);
    }

    public void setOnDataAvailableListener(OnDataAvailableListener l)
    {
        mOnDataAvailableListener = l;
    }

    /* 连接远程设备的回调函数 */
    @SuppressLint("NewApi")
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
       
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState)
        {
            String intentAction;
     
                if (newState == BluetoothProfile.STATE_CONNECTED)//连接成功
                {
                    intentAction = ACTION_GATT_CONNECTED;
                    //mConnectionState = STATE_CONNECTED;
                   // mConnectionState = Ble_Activity.LECONNECT_SUCCESS;
                    bloodactivity.mleHandler.obtainMessage(bloodactivity.LECONNECT_SUCCESS).sendToTarget();
               
                    
                    /* 通过广播更新连接状态 */
                    broadcastUpdate(intentAction);
                    Log.i(TAG, "Connected to GATT server.");
                    // Attempts to discover services after successful connection.
                   Log.i(TAG, "Attempting to start service discovery:"
                            + mBluetoothGatt.discoverServices());

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED)//连接失败
                {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    layaduankai=true;
                    Log.e("shibai111111111111111111", "bbbbbbbbbbbbbb");
                   // mConnectionState = STATE_DISCONNECTED;
                   // mConnectionState = Ble_Activity.LECONNECT_BREAK;
                    bloodactivity.mleHandler.obtainMessage(bloodactivity.LECONNECT_BREAK).sendToTarget();
                    Log.i(TAG, "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                }
           
            
        }
        /*
         * 重写onServicesDiscovered，发现蓝牙服务
         *
         * */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)//发现到服务
            {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.i(TAG, "--onServicesDiscovered called--");
            } else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                System.out.println("onServicesDiscovered received: " + status);
            }
        }
        /*
         * 特征值的读写
         * */
        @SuppressLint("NewApi")
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {

                //数据开始呼叫读 一个入口
                Log.i(TAG, "--onCharacteristicRead called--------------------------------");
                //从特征值读取数据
                byte[] sucString = characteristic.getValue();
                String string = new String(sucString);
                //将数据通过广播到Ble_Activity
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }

        }
        /*
         * 特征值的改变,数据从这里开始循环读取
         * */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic)
        {
      //      System.out.println("++++++++++++++++");

            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
          
            String return_back=toHex(characteristic.getValue());
  
              Log.e("<<<<<<<<<<<<<<shujv>>>>>>>>>>>>>>",return_back);
            

        }
        /*
         * 特征值的写
         * */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status)
        {

            Log.w(TAG, "--onCharacteristicWrite--: " + status);
            // 以下语句实现 发送完数据或也显示到界面上
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
        /*
         * 读描述值
         * */
        @SuppressLint("NewApi")
        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status)
        {
            // TODO Auto-generated method stub
            // super.onDescriptorRead(gatt, descriptor, status);
            Log.w(TAG, "----onDescriptorRead status: " + status);
            byte[] desc = descriptor.getValue();
            if (desc != null)
            {
                Log.w(TAG, "----onDescriptorRead value: " + new String(desc));
            }

        }
        /*
         * 写描述值
         * */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status)
        {
            // TODO Auto-generated method stub
            // super.onDescriptorWrite(gatt, descriptor, status);
            Log.w(TAG, "--onDescriptorWrite--: " + status);
        }
        /*
         * 读写蓝牙信号值
         * */
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
        {
            // TODO Auto-generated method stub
            // super.onReadRemoteRssi(gatt, rssi, status);
            Log.w(TAG, "--onReadRemoteRssi--: " + status);
            broadcastUpdate(ACTION_DATA_AVAILABLE, rssi);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status)
        {
            // TODO Auto-generated method stub
            // super.onReliableWriteCompleted(gatt, status);
            Log.w(TAG, "--onReliableWriteCompleted--: " + status);
        }

    };
    //广播意图
    private void broadcastUpdate(final String action, int rssi)
    {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, String.valueOf(rssi));
        sendBroadcast(intent);
    }
    //广播意图
    private void broadcastUpdate(final String action)
    {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /* 广播远程发送过来的数据 */
    public void broadcastUpdate(final String action,
                                final BluetoothGattCharacteristic characteristic) {

        final Intent intent = new Intent(action);
    
       //保存接收过来的数据
 //       final byte[] data = characteristic.getValue(); 
        //      System.out.print(bloodactivity.shishiflag); 
      //    int st=0;               
        celiangcuowuzt1=true; 
        datarec = characteristic.getValue();
        for (int i=0;i<datarec.length;i++){
            //将接受过来的数据保存到动态数组
            datarlist.add(datarec[i]);
        }
        if (datarlist.get(117)==(byte)0x02&&datarlist.get(118)==(byte)0x20&&datarlist.get(119)==(byte)0xDD&&datarlist.get(122)!=(byte)0xEE) {
        	int re1 = (int) (datarlist.get(122) << 8);
            int re = (int) ((re1 + datarlist.get(123)));
            int ss=(int)re;
       //     int ss=(int)re&0xff;
          tt=(float)ss/10;
           intent.putExtra(EXTRA_DATA2, tt);
           sendBroadcast(intent);
           Log.e("wancheng1111111111111", "gggggggggg");
           datarlist.removeAll(datarlist);
		}
        

}

    public class LocalBinder extends Binder {
        public BluetoothLeService getService()
        {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        close();
        return super.onUnbind(intent);
    }

  private final IBinder mBinder = new LocalBinder();
  
/*  public void onDestroy() {  
        Log.i("info", "BindService--->onDestroy()");  
        super.onDestroy();  
    
    } 
*/

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    /* service 中蓝牙初始化 */
    public boolean initialize()
    {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null)
        {   //获取系统的蓝牙管理器
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null)
            {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address
     *            The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The
     *         connection result is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    // 连接远程蓝牙
    @SuppressLint("NewApi")
    public boolean connect(final String address)
    {
        if (mBluetoothAdapter == null || address == null)
        {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect.
        if (mBluetoothDeviceAddress != null
                && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null)
        {
            Log.d(TAG,
                    "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect())//连接蓝牙，其实就是调用BluetoothGatt的连接方法
            {
               // mConnectionState = STATE_CONNECTING;
               // Ble_Activity.mleHandler.obtainMessage(Ble_Activity.LECONNECTING).sendToTarget();
                return true;
            } else
            {
                return false;
            }
        }
        /* 获取远端的蓝牙设备 */
        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        if (device == null)
        {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        /* 调用device中的connectGatt连接到远程设备 */
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        //mConnectionState = STATE_CONNECTING;
       // Ble_Activity.mleHandler.obtainMessage(Ble_Activity.LECONNECTING).sendToTarget();
        System.out.println("device.getBondState==" + device.getBondState());
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    /*
     * 取消连接
     *
     * */
    /**
     * @Title: disconnect
     * @Description: TODO(取消蓝牙连接)
     * @param
     * @return void
     * @throws
     */
    public void disconnect()
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();

    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    /**
     * @Title: close
     * @Description: TODO(关闭所有蓝牙连接)
     * @param
     * @return void
     * @throws
     */
    public void close()
    {
        if (mBluetoothGatt == null)
        {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic
     *            The characteristic to read from.
     */
    /**
     * @Title: readCharacteristic
     * @Description: TODO(读取特征值)
     * @param @param characteristic（要读的特征值）
     * @return void    返回类型
     * @throws
     */
    @SuppressLint("NewApi")
    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);

    }

    // 写入特征值
    @SuppressLint("NewApi")
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);

    }

    // 读取RSSi
    @SuppressLint("NewApi")
    public void readRssi()
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readRemoteRssi();
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic
     *            Characteristic to act on.
     * @param enabled
     *            If true, enable notification. False otherwise.
     */
 //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * @Title: setCharacteristicNotification
     * @Description: TODO(设置特征值通变化通知)
     * @param @param characteristic（特征值）
     * @param @param enabled （使能）
     * @return void
     * @throws
     */
    @SuppressLint("NewApi")
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor clientConfig = characteristic
                .getDescriptor(UUID
                        .fromString("00002902-0000-1000-8000-00805f9b34fb"));

        if (enabled)
        {
            clientConfig
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else
        {
            clientConfig
                    .setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        mBluetoothGatt.writeDescriptor(clientConfig);
    }

    /**
     * @Title: getCharacteristicDescriptor
     * @Description: TODO(得到特征值下的描述值)
     * @param @param 无
     * @return void
     * @throws
     */
    @SuppressLint("NewApi")
    public void getCharacteristicDescriptor(BluetoothGattDescriptor descriptor)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.readDescriptor(descriptor);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    /**
     * @Title: getSupportedGattServices
     * @Description: TODO(得到蓝牙的所有服务)
     * @param @return    无
     * @return List<BluetoothGattService>
     * @throws
     */
    public List<BluetoothGattService> getSupportedGattServices()
    {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();

    }
    public void wrte(byte[] bb) {
        // TODO Auto-generated method stub
        BluetoothGattService bGattService = mBluetoothGatt
                .getService(SERVIE_UUID);
        if (ChoiceHome.xindiankai1) {
            bGattService.getCharacteristic(UUID.fromString(bleactivty.HEART_RATE_MEASUREMENT));
        }else if (ChoiceHome.xueyakai1 ) {
            bGattService.getCharacteristic(UUID.fromString(bloodactivity.HEART_RATE_MEASUREMENT));
        } 
        BluetoothGattCharacteristic alertLevel = null;
        boolean status = false;
        @SuppressWarnings("null")
        int storedLevel = alertLevel.getWriteType();
        Log.e(TAG, "storedLevel() - storedLevel=" + storedLevel);
        alertLevel.setValue(bb);
        Log.e("发送的指令", "bb" + bb[0]);

        alertLevel.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        status = mBluetoothGatt.writeCharacteristic(alertLevel);
        Log.e(TAG, "writeLlsAlertLevel() - status=" + status);
        
    }
    // byte数组转换为十六进制字符串
    private String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString().toUpperCase();
    }
}