package com.yiran.push.mypaint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	
	private Button btn;
    private Button red;
    private Button black;
	private MyView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        red = (Button) findViewById(R.id.Red);
        black = (Button) findViewById(R.id.Black);

        view = (MyView) findViewById(R.id.draw);

        //view.PathSetter(Color.RED, 10 ,true);
        btn.setOnClickListener(clearBtn);
        black.setOnClickListener(blackPaint);
}





    //clear canvas
    private OnClickListener clearBtn = new OnClickListener(){
        public void onClick(View v) {
            view.clear();
        }
    };

    private OnClickListener blackPaint = new OnClickListener(){
        public void onClick(View v) {
            view.PathSetter(Color.BLACK, 10 ,true);
        }
    };
}
