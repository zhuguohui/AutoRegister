package com.trs.app.autoregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.trs.app.JSMethodConfig;
import com.trs.app.route.RouteRegisterHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("zzz", "onCreate: "+ JSMethodConfig.CLASSES);
        Log.i("zzz", "onCreate: "+ RouteRegisterHelper.CLASSES);
    }
}