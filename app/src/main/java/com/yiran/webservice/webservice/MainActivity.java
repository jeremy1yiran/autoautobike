package com.yiran.webservice.webservice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //WSDL文档中的命名空间
    private static final String nameSpace="http://WebXml.com.cn/";
    //WSDL文档中的URL
    private static final String endPoint="http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    //调用的方法
    private static final String methodName="getSupportCity";

    private ProgressDialog progressDialog ;

    public static final String waitTitle = "努力加载中......" ;
    public static final String waitMessage = "" ;
     private TextView  textTitle ;
     private  String   title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTitle =(TextView)this.findViewById(R.id.titleview);
        ProgressDialog.show(MainActivity.this, waitTitle,waitMessage); //等待加载的dialog
        Thread t1 = new Thread(Ws_run,"t1");
              t1.start();
    }

    private Handler handler =new Handler(){
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //UI的操作必须放到handler中处理
            textTitle.setText(title);
            //只要执行到这里就关闭对话框
            progressDialog.dismiss();
        }
    };


    Runnable Ws_run = new Runnable() {
        @Override
        public void run() {
            GetMessage();
        }
    };

    private void GetMessage() {
        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        //假设有参数
        // rpc.addProperty("mobileCode", phoneSec);
        //  rpc.addProperty("userId", "");
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);


        HttpTransportSE transport = new HttpTransportSE(endPoint);
        //AndroidHttpTransport transport=new AndroidHttpTransport(serviceURL);
        try {
            // 调用WebService
            transport.call(nameSpace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.getResponse();
            // 获取返回的结果
            title = object.getProperty(0).toString();
            // 将WebService返回的结果显示在TextView中

            System.out.println(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取返回的数据


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
