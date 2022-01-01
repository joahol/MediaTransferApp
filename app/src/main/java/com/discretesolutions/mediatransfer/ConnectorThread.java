package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

/*
            CLASS IS SUPPOSED TO BE REMOVED
 */
public class ConnectorThread extends Thread {

    private final BluetoothDevice btDevice;
    private final BluetoothSocket btSocket;

    public ConnectorThread(BluetoothDevice device) {
        btDevice = device;
        BluetoothSocket temporary = null;
        BluetoothDevice btDevice = device;
        // UUID myuuid = UUID.fromString("00001108-0000-1000-8000-00805F9B3000");
        UUID myuuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            if (btDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                Log.v("BT:", "Device not bondned");
            }
            // ParcelUuid[] uuids = btDevice.getUuids();
            //temporary = btDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            // temporary = btDevice.createInsecureRfcommSocketToServiceRecord(myuuid);
            temporary = (BluetoothSocket) btDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (Exception e) {
            Log.e("BTHelper", "Issue on creating a bluetooth socket " + e);
        }

        btSocket = temporary;
        Log.v("BTSOCKET:", String.valueOf(btSocket.getConnectionType()));


    }

    public void run() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:Adapter", String.valueOf(bluetoothAdapter.isEnabled()));
        try {
            bluetoothAdapter.cancelDiscovery();
            btSocket.connect();
            Log.v("Run:", btSocket.toString());
        } catch (Exception e) {
            Log.v("BTHelper:Run:Error", "Some issue with" + e);
            //on exception close down the connection
            try {
                Log.v("Shtting down thread", "1");
                btSocket.close();
                Log.v("Shtting down thread", "2");
            } catch (Exception f) {
                Log.v("ConnectorThread:",
                        "Closing connection" + f);
                return;

            }
            ThreadedConnection tc = new ThreadedConnection(btSocket);
            Log.v("TC", "fireing up tc");
            tc.run();
            Log.v("Connected:", tc.toString());
            Log.v("Connected:", btSocket.toString());
            Log.v("BT Status", String.valueOf(btSocket.isConnected()));
            return;
        }

    }

    public void cancel() {
        try {

        } catch (Exception e) {
            Log.e("BTHelper:Cancel", "Trouble in disconnecting socket" + e);
        }
    }
}



