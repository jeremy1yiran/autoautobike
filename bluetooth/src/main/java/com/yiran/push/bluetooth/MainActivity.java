package com.yiran.push.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;


public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private BluetoothAdapter mBluetoothAdapter ;


    private ListView listView;
    private Button button;


    private List<String> bluetoothDevices = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    //private final UUID MY_UUID = UUID.fromString("xcfg90mn-xcvb-12ws-9iu8-0p9o8i7u6y5t");
    private final UUID MY_UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c27bae3");
    private final String NAME="BlueTooth_Socket";
    private BluetoothSocket clientSocket;
    private BluetoothDevice device;
    private OutputStream os;
    private AcceptThread acceptThread;

    public final static String EXTRA_ADDRESS = "com.yiran.push.bluetooth.MainActivity.address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView  = (ListView)findViewById(R.id.lvDevices);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_Search();
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       //搜索已经连接的蓝牙
        Set<BluetoothDevice> set= mBluetoothAdapter.getBondedDevices();
        if(set.size()>0){
             for(BluetoothDevice device : set){
                 bluetoothDevices.add(device.getName()+":"+device.getAddress());
             }
        }
        arrayAdapter = new ArrayAdapter<String>(this,
                               android.R.layout.activity_list_item,

                android.R.id.text1,bluetoothDevices);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        //解除绑定  BluetoothDevice.ACTION_FOUND   BluetoothAdapter.ACTION_DISCOVERY_FINISHED
        IntentFilter filter  = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver, filter);


        acceptThread = new AcceptThread();
        acceptThread.start();


    }



    public void onClick_Search(){
         setProgressBarIndeterminate(true);
         setTitle("seaching.....");
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice  device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    bluetoothDevices.add(device.getName() + ":"
                            + device.getAddress() + "\n");
                    arrayAdapter.notifyDataSetChanged();
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("连接蓝牙设备");

            }
        }
    };

    @Override
    //onclick 是APP作为客户端进行了链接
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                 String s = arrayAdapter.getItem(position);
                 String address = s.substring(s.indexOf(":")+1).trim();

                 Intent intent = new Intent(this,DirectActivity.class);
                 intent.putExtra(EXTRA_ADDRESS,address);
                 startActivity(intent);


        /**try{
            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();
            }
            try{
                    if(device==null){
                        device = mBluetoothAdapter.getRemoteDevice(address);
                    }

            }catch(Exception e){
            }
            if(clientSocket==null){
                clientSocket  = device.createRfcommSocketToServiceRecord(MY_UUID);
                clientSocket.connect();
                os = clientSocket.getOutputStream();
            }
            if(null!=os){
                os.write("0".getBytes());
            }


        }catch(Exception e){


        }**/

     }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, String.valueOf(msg.obj),
                    Toast.LENGTH_LONG).show();
            super.handleMessage(msg);
        }
    };



    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private InputStream is;
        private OutputStream os;

        public AcceptThread() {
            try {
                serverSocket = mBluetoothAdapter
                        .listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        public void run() {
            try
            {
                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();

                while(true)
                {
                    byte[] buffer =new byte[128];
                    int count = is.read(buffer);
                    Message msg = new Message();
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    handler.sendMessage(msg);
                }
            }
            catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

}
