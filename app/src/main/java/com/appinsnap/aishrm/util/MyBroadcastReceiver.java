package com.appinsnap.aishrm.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private MyListener listener;

    public MyBroadcastReceiver(MyListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract the data from the intent
        String data = intent.getStringExtra("key");

        // Pass the data to the listener
        if (listener != null) {
            listener.onDataReceived(data);
        }
    }

    public interface MyListener {
        void onDataReceived(String data);
    }
}

