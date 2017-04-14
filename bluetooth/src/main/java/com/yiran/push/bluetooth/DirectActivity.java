package com.yiran.push.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.OutputStream;
import java.util.UUID;

public class DirectActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter ;
    private BluetoothDevice device;
    private BluetoothSocket clientSocket;
    private  OutputStream os;

    private final UUID MY_UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c27bae3");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        TextView textView = (TextView)findViewById(R.id.testView);
        textView.setTextSize(30);
        textView.setText(message);
        setContentView(R.layout.activity_direct);


        //获取button
        Button btn_brading = (Button)findViewById(R.id.braking);
        btn_brading.setOnClickListener(braking);

        Button btn_accelerator =(Button)findViewById(R.id.accelerator);
        btn_accelerator.setOnClickListener(accelerator);
    }

    private void sendMessage(String message){
        if(null!=message && "".equals(message)) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            try {
                if (device == null) {
                    device = mBluetoothAdapter.getRemoteDevice(message);
                }
                if (clientSocket == null) {
                    clientSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    clientSocket.connect();
                    os = clientSocket.getOutputStream();
                }
                //if(null!=os){
                //    os.write(textView.getText().toString().getBytes());
                //}

            } catch (Exception e) {
            }
        }
    }

     View.OnClickListener braking = new View.OnClickListener(){
            public void onClick(View view){
                sendMessage("P");
            }

    };

    View.OnClickListener accelerator = new View.OnClickListener(){
        public void onClick(View view){
            sendMessage("D");
        }
    };

}
