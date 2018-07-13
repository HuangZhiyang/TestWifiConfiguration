package com.hzy.testwificonfiguration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager mConnectivityManager;
    private WifiManager  mWifiManager;
    private Context mContext;
    private  static final  String TAG = "TestWifiConfig";
    TextView tvWifiInfo;
    String  strWifiInfo="";

    private static final int SECURITY_NONE = 0;
    private static final int SECURITY_WEP = 1;
    private static final int SECURITY_PSK = 2;
    private static final int SECURITY_EAP = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initViews();
        initVars();

    }

    private void initViews(){
        tvWifiInfo = (TextView) findViewById(R.id.textWifiInfo);
    }
    private void initVars(){

        mConnectivityManager= (ConnectivityManager) mContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager  = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        getNetworkInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private  void getNetworkInfo() {
        strWifiInfo = "";
        int networkId;
        NetworkInfo networkInfo=mConnectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null &&  networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.e(TAG,"Wifi is Connected");
            strWifiInfo += "Wifi is Connected\n\n";
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if(wifiInfo != null) {

                Log.e(TAG, "Network ID" + wifiInfo.getNetworkId());
                strWifiInfo += ("Network ID" + wifiInfo.getNetworkId()) + '\n';
                networkId =  wifiInfo.getNetworkId();

                Log.e(TAG, "BSSID" + wifiInfo.getBSSID());
                strWifiInfo += ("BSSID" + wifiInfo.getBSSID()) + '\n';

                Log.e(TAG, "SSID" + wifiInfo.getSSID());
                strWifiInfo += ("SSID" + wifiInfo.getSSID()) + '\n';

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.e(TAG, "Freq" + wifiInfo.getFrequency());
                    strWifiInfo += ("Freq" + wifiInfo.getFrequency()) + '\n';
                }
                Log.e(TAG, "linkSpeed" + wifiInfo.getLinkSpeed());
                strWifiInfo += ("linkSpeed" + wifiInfo.getLinkSpeed()) + '\n';

                Log.e(TAG, "Our IpAdress" + wifiInfo.getIpAddress());
                strWifiInfo += ("Our IpAdress" + wifiInfo.getIpAddress()) + '\n';

               // Log.e(TAG, "Our MacAddress" + wifiInfo.getMacAddress());
                //strWifiInfo += ("Our MacAddress" + wifiInfo.getMacAddress()) + '\n';
                strWifiInfo += "\n\n";

                if(networkId != -1) {
                    getWifiConfiguration(networkId);
                }
            }
            tvWifiInfo.setText(strWifiInfo);
        }else {
            Log.e(TAG,"Wifi not connected");
            tvWifiInfo.setText("Wifi not connected");

        }
    }

    private void getWifiConfiguration(int networkId){

        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        for(WifiConfiguration config:configs){
            if(config.networkId ==  networkId){
                Log.e(TAG,"哈哈哈哈哈");
                Log.e(TAG,"config.BSSID" + config.BSSID);
                strWifiInfo += ("config.BSSID" + config.BSSID + '\n');

                Log.e(TAG,"config.SSID"  + config.SSID);
                strWifiInfo += ("config.SSID" + config.SSID + '\n');
                Log.e(TAG,"config.networkId" + config.networkId);

                strWifiInfo += ("config.networkId" + config.networkId + '\n');
                int secure_type = getSecurity(config);
                Log.e(TAG,"secure Type = " + secure_type);
                strWifiInfo += ("secure Type" + secure_type + '\n');

            }
        }
    }

    int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }
}
