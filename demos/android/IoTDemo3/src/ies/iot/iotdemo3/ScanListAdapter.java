package ies.iot.iotdemo3;

import ies.iot.demolib.ble.BeaconScanInfo;
import ies.iot.demolib.utils.DemoSettings;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ScanListAdapter extends BaseAdapter {
    
    private final static String TAG_ = 
            ScanListAdapter.class.getSimpleName();
    private ArrayList<BeaconScanInfo> beacon_list_;
    private LayoutInflater inflator_;
    private Activity context_;
    
    public ScanListAdapter(Activity context) {
        context_ = context;
        beacon_list_ = new ArrayList<BeaconScanInfo>();
        inflator_ = context_.getLayoutInflater();
    }
    
    @Override
    public int getCount() {
        return beacon_list_.size();
    }

    @Override
    public Object getItem(int position) {
        return beacon_list_.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder view_holder;
        
        // General ListView optimization code.
        if (view == null) {
            view = inflator_.inflate(R.layout.scan_list_item, null);
            view_holder = new ViewHolder();
            view_holder.device_address_ = 
                    (TextView) view.findViewById(R.id.tv_address);
            view_holder.device_name_ = 
                    (TextView) view.findViewById(R.id.tv_name);
            view_holder.device_rssi_ =
                    (TextView)view.findViewById(R.id.tv_rssi);
            view.setTag(view_holder);
        } else {
            view_holder = (ViewHolder) view.getTag();
        }

        BeaconScanInfo bsi = beacon_list_.get(position);
        final String name = bsi.get_name();
        if (name != null && name.length() > 0)
            view_holder.device_name_.setText(name);
        else
            view_holder.device_name_.setText("Unknown Device");
        
        view_holder.device_address_.setText(bsi.get_address());
        view_holder.device_rssi_.setText("" + bsi.get_rssi());

        return view;
    }

    public void clear() {
        beacon_list_.clear();
        notifyDataSetChanged();
    }
    
    public void update_device (BluetoothDevice device, int rssi) {
        //
        // Check if exist
        //
        BeaconScanInfo bsi = get_scan_object(device.getAddress());
        if (bsi == null) {
            //
            // Create a new one
            //
            bsi = new BeaconScanInfo(
                    device,
                    device.getName(), 
                    device.getAddress(),
                    rssi);
            beacon_list_.add(bsi);
        }

        //
        // Only update the RSSI value for existing beacon device.
        //
        bsi.set_rssi(rssi);
        notifyDataSetChanged();
    }
    
    public void refresh_disappeared_device() {
        boolean need_refresh = false;
        for (Iterator<BeaconScanInfo> it = beacon_list_.iterator(); it.hasNext();) {
            BeaconScanInfo bsi = it.next();
            if (bsi.is_expired()) {
                it.remove();
                need_refresh = true;
            }
        }
        if (need_refresh) {
            notifyDataSetChanged();
        }
    }
    
    private BeaconScanInfo get_scan_object(String address) {
        for (BeaconScanInfo item:beacon_list_) {
            if (item.get_address().compareToIgnoreCase(address) == 0) {
                return item;
            }
        }
        return null;
    }
    
    class ViewHolder {
        BluetoothDevice device_;
        TextView device_name_;
        TextView device_address_;
        TextView device_rssi_;
    }
    
    private OnItemClickListener device_list_click_listener_ = 
            new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            ViewHolder view_holder = (ViewHolder) arg1.getTag();
            BeaconScanInfo bsi = beacon_list_.get(arg2);
            if (!bsi.get_name().equalsIgnoreCase("sensortag")) {
                new AlertDialog.Builder(context_)
                    .setMessage("Not a Sensor Device")
                    .setTitle("Error")
                    .setCancelable(true)
                    .show();
                return;
            }
            
            ((ScanActivity)context_).getService().getBleManager().stopScan();
            
            /**
            DemoSettings.getInstance().setDeviceName(context_,
                    bsi.get_name());
            DemoSettings.getInstance().setDeviceAddress(context_, 
                    bsi.get_address());
            Intent intent = new Intent(context_, DashboardActivity.class);
            context_.startActivity(intent);
            context_.finish();
            **/
        }
    };
    
    public OnItemClickListener get_item_click_listener() {
        return device_list_click_listener_;
    }
}
