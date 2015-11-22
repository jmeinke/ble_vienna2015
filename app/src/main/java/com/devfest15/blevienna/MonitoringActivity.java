package com.devfest15.blevienna;


import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import io.realm.Realm;


public class MonitoringActivity extends AppCompatActivity {
    protected static final String TAG = "MonitoringActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}