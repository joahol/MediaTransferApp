package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ThreadedConnection {
    private byte[] buffer;
    private BluetoothSocket btSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    Handler handler;

    public ThreadedConnection(BluetoothSocket socket) {
        btSocket = socket;

        InputStream ins = null;
        OutputStream ous = null;
        try {
            Log.v("ThreadedConnection:", "Setting up streams");
            ins = btSocket.getInputStream();
            ous = btSocket.getOutputStream();
            Log.v("ThreadedConnection:", "stream in:" + ins.available() + " Stream out:" + ous.toString());
        } catch (Exception e) {
            Log.e("ThreadedConnection:error:", e.toString());
        }
        inputStream = ins;
        outputStream = ous;


        Log.v("Threaded Connection", "finished settting up " + btSocket.isConnected());
    }

    public void run() {
        buffer = new byte[1024];
        int bytes;
        Log.v("ThreadedConnection:", "starting up");
        while (true) {
            try {
                bytes = inputStream.read(buffer);
                Message rmsg = handler.obtainMessage(BTService.BTServiceMessages.READ, bytes, -1, buffer);
                rmsg.sendToTarget();

            } catch (Exception e) {
                Log.v("BTService:error ", e.toString());
                break;
            }

        }
        Log.v("ThreadedConnection:", "finnished");
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            Message wmsg = handler.obtainMessage(BTService.BTServiceMessages.WRITE, -1, -1, buffer);
            wmsg.sendToTarget();

        } catch (Exception e) {
            Log.e("Write:Error:", e.toString());
            Message wem = handler.obtainMessage(BTService.BTServiceMessages.FEEDBACK_USER);
            Bundle bundle = new Bundle();
            bundle.putString("FEEDBACKMESSAGE", "other device could not receive message");
            wem.setData(bundle);
            handler.sendMessage(wem);
        }
    }

    /*
     *@description Shut down connection to sockets
     */
    public void closeConnectionsl() {
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.e("BTService:", "Trouble closing socket ", e);
        }
    }
}