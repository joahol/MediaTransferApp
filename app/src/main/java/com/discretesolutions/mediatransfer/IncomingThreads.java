package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.UUID;

public class IncomingThreads extends Thread {
    private final BluetoothServerSocket serverSocket;
    String name = "mediaservice";
    UUID uuid = UUID.randomUUID();

    private final boolean flag = true;

    public IncomingThreads() {
        BluetoothAdapter btadapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket temporary = null;
        try {
            temporary = btadapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid);
        } catch (Exception e) {
        }
        serverSocket = temporary;
    }

    public void run() {
        BluetoothSocket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                if (socket.isConnected()) {
                    Log.v("BTConnector", "connection successfull");
                    Log.v("BTconnector", socket.getRemoteDevice().getName() + " " + socket.getRemoteDevice().getAddress());
                }
            } catch (Exception a) {
                Log.v("SomeBug", a.getMessage());
                try {
                    socket.close();
                } catch (Exception v) {
                    Log.v("Bug", v.getMessage());
                }
                break;
            }
        }
    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (Exception p) {
            Log.v("BUG  Cancel ", p.getMessage());
        }
    }
}
