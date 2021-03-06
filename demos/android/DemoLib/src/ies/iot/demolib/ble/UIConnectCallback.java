package ies.iot.demolib.ble;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public abstract class UIConnectCallback {
    private String TAG = getClass().getSimpleName();
    
    public void onConnected() {
        Log.v(TAG, "onConnected");
    }
    
    public void onDisconnected() {
        Log.v(TAG, "onDisconnected");
    }
    
    public void onServiceDiscoveried() {
        Log.v(TAG, "onServiceDiscovried");
    }
    
    public void onUpdateSensorValue() {
        //Log.v(TAG, "onUpdateSensorValue");
    }
    
    public void onScanCallback(BluetoothDevice device, int rssi) {
        Log.v(TAG, "onScanCallback: " + device.getName() + " " + 
                device.getAddress() + " " + rssi);
    }
}
