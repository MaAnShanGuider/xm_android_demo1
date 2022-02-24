package com.zhangxueyou.android2.activity.settings.index;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.zhangxueyou.android2.R;
import com.zhangxueyou.android2.databinding.FragmentSettingsIndex1Binding;

import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.id3xx.IDCardReader;
import com.zkteco.id3xx.meta.IDCardInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SettingsIndex1Fragment extends Fragment {

    ArrayAdapter<String> mAdapter;
    ArrayList<String> list;
    public ListView mList;
    private FragmentSettingsIndex1Binding binding;
    private String TAG = "蓝牙：";
    private View view;
    private WorkThread workThread = null;
    public AlertDialog dialog;
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter bluetoothAdapter;
    public ScanCallback scanCallback;
    IDCardReader idCardReader = null;
    private CountDownLatch countdownLatch = new CountDownLatch(1);
    boolean mbStop = true;

    private EditText infoName;
    private EditText infoSex;
    private EditText infoNation;
    private EditText infoBirth;
    private EditText infoAddress;
    private EditText infoIdcard;
    private EditText infoCertifying;
    private EditText infoData;
    private ImageView image;

    private boolean isExitFinger = false;
    private boolean isUpload = false;
    private boolean isVerify = true;
    private byte[] template;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        binding = FragmentSettingsIndex1Binding.inflate(inflater, container, false);
        view = inflater.inflate(R.layout.fragment_settings_index_1, container, false);

        TextView findBlueToothBtn = view.findViewById(R.id.btn_findBlueTooth);
        TextView closeBlueToothBtn = view.findViewById(R.id.btn_closeBlueTooth);
        TextView readCardBtn = view.findViewById(R.id.btn_readCard);
        TextView disconnectBtn = view.findViewById(R.id.btn_disconnect);


        // 蓝牙适配器
        bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
//        bluetoothAdapter.enable();//8.0版本 使用这个可以弹窗询问开启 其他版本则是不提示启动
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        IntentFilter mFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, mFilter1);
        // 注册搜索完时的receiver
        IntentFilter mFilter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, mFilter2);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);

        dialog = new AlertDialog.Builder(getContext()).setTitle("蓝牙列表").setView(dialogView).setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        try {
                            if (bluetoothAdapter.isDiscovering()) {
                                Log.e(TAG, "OnBnSearch: 停止搜索蓝牙");

                                bluetoothAdapter.cancelDiscovery();
                            }
                        } catch (SecurityException ex) {
                            Log.v("蓝牙错误:", ex.getMessage());
                        }

                        dialog.dismiss();
                    }
                }).create();

        mList = dialogView.findViewById(R.id.bluetooth_list1);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
               try {
                   if (bluetoothAdapter.isDiscovering()) {
                       bluetoothAdapter.cancelDiscovery();
                   }
                   String text = list.get(arg2);
                   int a = text.indexOf("|");
                   String mac = text.substring(a + 1);
                   BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac);
                   try {
                       connect(device, text.split("\\|")[0]);
                   } catch (Exception e) {
                       // TODO: handle exception
                       Toast.makeText(getContext(), "连接失败", Toast.LENGTH_SHORT).show();
                   }
               } catch (SecurityException ex) {
                   // TODO: handle exception
                   idCardReader = null;
                   Log.v("蓝牙连接结果：", "失败");

               }

            }
        });
        findBlueToothBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(TAG, "startBt: 开始开启蓝牙-1");
                OnBnSearch();
            }
        });
        closeBlueToothBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                closeBt();
            }
        });
        // 开始读卡
        readCardBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OnBnRead();
            }
        });
        // 断开链接
        disconnectBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OnBnDisconn();
            }
        });

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public void OnBnDisconn()
    {
        if (null == idCardReader)
        {
            return;
        }
        if (!mbStop)
        {
            mbStop = true;
            try {
                countdownLatch.await(5*1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        idCardReader.closeDevice();
        idCardReader = null;
        Log.e(TAG, "OnBnDisconn: 断开设备成功");


    }
    public void OnBnRead()
    {
        if (null == idCardReader)
        {
            Log.e(TAG, "OnBnRead: 请先连接设备");

            return;
        }
        if (!mbStop)
        {
            return;
        }
        mbStop = false;
        workThread = new WorkThread();
        workThread.start();// 线程启动
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // TODO Auto-generated method stub
        getContext().unregisterReceiver(mReceiver);
    }

    public void onResume(){
        super.onResume();
        checkPermission();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
        }
    }
    public void OnBnSearch() {
        list = new ArrayList<>();
        checkPermission();
        if (bluetoothAdapter.isDiscovering()) {
                    Log.e(TAG, "OnBnSearch: 停止搜索蓝牙");

            bluetoothAdapter.cancelDiscovery();
        }
        Log.e(TAG, "OnBnSearch: 开始搜索蓝牙");

        bluetoothAdapter.startDiscovery();
        mAdapter = new ArrayAdapter<>(getContext(), R.layout.tv, list);
        mList.setAdapter(mAdapter);
        dialog.show();
    }
    // 关闭蓝牙
    public void closeBt() {
        if (bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            bluetoothAdapter.disable();
        }
        Log.e(TAG, "closeBt: 关闭蓝牙");
    }

    private void connect(BluetoothDevice device,String bluetooth) {
        try {
            idCardReader = new IDCardReader();
            BluetoothSocket mBluetoothSocket = device.createRfcommSocketToServiceRecord(IDCardReader.myuuid);
            mBluetoothSocket.connect();
            InputStream mInputStream = mBluetoothSocket.getInputStream();
            OutputStream mOutputStream = mBluetoothSocket.getOutputStream();
            idCardReader.init(mInputStream,mOutputStream);
            dialog.dismiss();
//            textView.setText(bluetooth);
            Log.v("蓝牙连接结果：", "成功");

        } catch (SecurityException | IOException ex) {
            // TODO: handle exception
            idCardReader = null;
            Log.v("蓝牙连接结果：", "失败");

        }
    }
    private class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!mbStop) {
                if (!ReadCardInfo())
                {
                    Log.v("读卡状态：","请放卡...");
                } else {
                    Log.v("读卡状态：","读卡成功，请放入下一张卡");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countdownLatch.countDown();
        }
    }
    private boolean ReadCardInfo()
    {
        if(!idCardReader.sdtFindCard()){
            return false;
        }else{
            if(!idCardReader.sdtSelectCard()){
                return false;
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v("读卡状态：","正在读卡...");

//                resetContent();
            }
        });
        final IDCardInfo idCardInfo = new IDCardInfo();
        if (idCardReader.sdtReadCard(1, idCardInfo))
        {
            long time=System.currentTimeMillis();
            final Calendar mCalendar=Calendar.getInstance();
            mCalendar.setTimeInMillis(time);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("身份证输出：", "姓名："+idCardInfo.getName());
                    Log.v("身份证输出：", "性别："+idCardInfo.getSex());
                    Log.v("身份证输出：", "民族："+idCardInfo.getNation());
                    Log.v("身份证输出：", "出生年月："+idCardInfo.getBirth());
                    Log.v("身份证输出：", "地址："+idCardInfo.getAddress());
                    Log.v("身份证输出：", "身份证号码："+idCardInfo.getId());
                    Log.v("身份证输出：", "发放部门："+idCardInfo.getDepart());
                    Log.v("身份证输出：", "有效期限："+idCardInfo.getValidityTime());
                }
            });
            return true;
        }
        else
        {
            //playSound(9, 0);
        }
        Log.v("读卡状态：","读卡失败...");
        return false;
    }
    private void resetContent()
    {

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String action = arg1.getAction();//64:C1:B5:3F:A9:90
            //DC:0D:30:04:23:1C
            System.out.println("==========此时蓝牙的状态是====11===="+action);
            Log.e("触发onReceive:", "我丢啊");
            Log.e("触发onReceive:", String.valueOf(action.equals(BluetoothDevice.ACTION_FOUND)));
            Log.e("触发onReceive:", String.valueOf(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)));

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    Log.e("Name:" + device.getName(), "MAC:" + device.getAddress());
                    String te = device.getName() + "|" + device.getAddress();
                    if (!list.contains(te)) {
                        list.add(te);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (SecurityException ex) {
                    Log.v("蓝牙错误:", ex.getMessage());
                }

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                // setProgressBarIndeterminateVisibility(false);
//                getParentFragment()
            }
        }
    };
}