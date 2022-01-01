package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

class ConnectThread extends Thread {
    private static final String TAG = "";
    private final BluetoothSocket socket;
    private final BluetoothDevice mmDevice;
    com.discretesolutions.mediatransfer.TransferModel transfermodel;

    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device, com.discretesolutions.mediatransfer.TransferModel tm) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        transfermodel = tm;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        socket = tmp;
    }

    public void run() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();

        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket cold not be closed", e);
            }
            return;
        }

        sendPictures(socket);
    }

    private void sendPictures(BluetoothSocket socket) {
        Log.v("manageMyConnectedSocket", socket.getRemoteDevice().getName() + " " + socket.getRemoteDevice().getAddress());
        com.discretesolutions.mediatransfer.ImageTools its = new com.discretesolutions.mediatransfer.ImageTools(transfermodel.getContext());
        String path = transfermodel.popImagePath();
        String filename = its.getFileName(path);
        Log.v("...", filename + " " + path);
        byte[] data = its.getImage(path);
        // int chunckSize = 1024;
        try {
            OutputStream ous = socket.getOutputStream();
            ous.write(data.length);
            ous.write(data, 0, data.length);
            ous.flush();
            ous.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}