package com.netanel.logclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LogActivity";
    ILogService logService;
    LogConnection conn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Request bind to the service
        conn = new LogConnection(); //
        Intent intent = new Intent("com.netanel.logservice.ILogService"); //
        intent.putExtra("version", "1.0"); //
        bindService(intent, conn, Context.BIND_AUTO_CREATE); //
// Attach listener to button
        ((Button) findViewById(R.id.buttonClick)).setOnClickListener(this);
    }
    class LogConnection implements ServiceConnection { //
        public void onServiceConnected(ComponentName name, IBinder service) { //
            logService = ILogService.Stub.asInterface(service); //
            Log.i(TAG, "connected");
        }
        public void onServiceDisconnected(ComponentName name) { //
            logService = null;
            Log.i(TAG, "disconnected");
        }
    }
    public void onClick(View button) {
        try {
            logService.log_d("LogClient", "Hello from onClick()"); //
            Message msg = new Message(Parcel.obtain()); //
            msg.setTag("LogClient");
            msg.setText("Hello from inClick() version 1.1");
            logService.log(msg); //
        } catch (RemoteException e) { //
            Log.e(TAG, "onClick failed", e);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
        unbindService(conn); //
        logService = null;
    }
}