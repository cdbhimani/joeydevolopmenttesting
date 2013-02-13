package com.joey.chain;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.provider.Settings;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
 
public class MainActivity extends AndroidApplication {
	 WifiLock wifiLock;
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        		             
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;      
              
       wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
        	    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire(); 
        initialize(new ReactorApp(), cfg);
    }
    @Override
    protected void onDestroy() {   
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	wifiLock.release();
    }
}