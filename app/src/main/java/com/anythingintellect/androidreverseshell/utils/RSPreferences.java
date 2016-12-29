package com.anythingintellect.androidreverseshell.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ishan.dhingra on 28/12/16.
 */

public class RSPreferences {

    private static final String DEFAULT_HOST = "192.168.1.2";
    private static final int DEFAULT_PORT = 443;

    private static final String PREF_NAME = "reverseShell";
    private static final String PREF_HOST = "host";
    private static final String PREF_PORT = "port";
    private SharedPreferences sharedPreferences;

    private RSPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static RSPreferences getInstance(Context context) {
        return new RSPreferences(context);
    }

    public void setHost(String host) {
        sharedPreferences.edit().putString(PREF_HOST, host).apply();
    }

    public String getHost() {
        return sharedPreferences.getString(PREF_HOST, DEFAULT_HOST);
    }

    public void setPort(int port) {
        sharedPreferences.edit().putInt(PREF_PORT, port).apply();
    }

    public int getPort() {
        return sharedPreferences.getInt(PREF_PORT, DEFAULT_PORT);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


}
