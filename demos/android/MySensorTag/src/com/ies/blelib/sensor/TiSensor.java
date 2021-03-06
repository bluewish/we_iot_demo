package com.ies.blelib.sensor;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.hardware.Sensor;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public abstract class TiSensor<T> extends BleSensor<T> {
    //
    // See the http://processors.wiki.ti.com/index.php/SensorTag_User_Guide
    //
    private String UUID_TI_BASE = "F0000000-0451-4000-B000-000000000000";

    /**
     * Gyroscope, Magnetometer, Barometer, IR temperature
     * all store 16 bit two's complement values in the awkward format
     * LSB MSB, which cannot be directly parsed as getIntValue(FORMAT_SINT16, offset)
     * because the bytes are stored in the "wrong" direction.
     *
     * This function extracts these 16 bit two's complement values.
     * */
    public static Integer shortSignedAtOffset(BluetoothGattCharacteristic c, int offset) {
        Integer lowerByte = c.getIntValue(FORMAT_UINT8, offset);
        if (lowerByte == null)
            return 0;
        Integer upperByte = c.getIntValue(FORMAT_SINT8, offset + 1); // Note: interpret MSB as signed.
        if (upperByte == null)
            return 0;

        return (upperByte << 8) + lowerByte;
    }

    public static Integer shortUnsignedAtOffset(BluetoothGattCharacteristic c, int offset) {
        Integer lowerByte = c.getIntValue(FORMAT_UINT8, offset);
        if (lowerByte == null)
            return 0;
        Integer upperByte = c.getIntValue(FORMAT_UINT8, offset + 1); // Note: interpret MSB as unsigned.
        if (upperByte == null)
            return 0;

        return (upperByte << 8) + lowerByte;
    }    
}
