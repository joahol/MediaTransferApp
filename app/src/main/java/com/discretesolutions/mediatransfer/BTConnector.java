package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

/*
@description class handles connection to a remote bt device where the remote acts as a server socket.
 */
public class BTConnector extends Thread {
    private final BluetoothDevice btServerDevice;
    private final BluetoothSocket btServerSocket;
    UUID myuuid;//= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /*
    @description Creates a basic BTConnector
    @parameter takes the target BTDevice as parameter and base the connection.
     */
    public BTConnector(BluetoothDevice BTServer) {
        System.out.println(("BTConnector"));
        btServerDevice = BTServer;
        ParcelUuid[] uuuids = btServerDevice.getUuids();
        myuuid = uuuids[0].getUuid();
        BluetoothSocket temporary = null;
        try {
            //  temporary = BTServer.createRfcommSocketToServiceRecord(myuuid);
            Log.v("BTDEV:", btServerDevice.getAddress() + " :" + btServerDevice.getName());
            temporary = btServerDevice.createRfcommSocketToServiceRecord(myuuid); //createInsecureRfcommSocketToServiceRecord(myuuid);

        } catch (Exception a) {
            Log.v("BTConnector: Constructor", a.getMessage());
        }
        btServerSocket = temporary;
        try {
            btServerSocket.connect();
        } catch (Exception a) {
        }
    }

    /*
        @description starting up the thread.
    */
    public void run() {
        try {
            Log.v("Running socket:", String.valueOf(btServerSocket.isConnected()));
            btServerSocket.connect();
            Log.v("Running socket:", String.valueOf(btServerSocket.isConnected()));
        } catch (Exception a) {
            a.printStackTrace();
        }
    }


    /*
        @description Closing the serversocket
    */
    public void cancel() {
        try {
            btServerSocket.close();
        } catch (Exception e) {
            Log.v("Exception: BTConnector.cancel:", e.getMessage());
        }
    }
}
