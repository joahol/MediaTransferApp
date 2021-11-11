package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.UUID;

public class BTHelper extends Thread {

    private final BluetoothDevice btDevice;
    private final BluetoothSocket btSocket;

    public BTHelper(BluetoothDevice device) {
        btDevice = device;
        BluetoothSocket temporary = null;
        BluetoothDevice btDevice = device;
        UUID myuuid = UUID.randomUUID();//CHECK BACK LATER, IT might be better to make it based on some parameter
        try {
            temporary = btDevice.createRfcommSocketToServiceRecord(myuuid);
        } catch (Exception e) {
            Log.e("BTHelper", "Issue on creating a bluetooth socket" + e);
        }
        btSocket = temporary;
    }

    public void run() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            bluetoothAdapter.cancelDiscovery();
            btSocket.connect();
        } catch (Exception e) {
            Log.e("BTHelper:Run:Error", "Some issue with" + e);
            //on exception close down the connection
            try {
                btSocket.close();
            } catch (Exception f) {

            }
            return;
        }
        getConnectedSocket(btSocket);
    }

    public void cancel() {
        try {

        } catch (Exception e) {
            Log.e("BTHelper:Cancel", "Trouble in disconnecting socket" + e);
        }
    }
}


}
