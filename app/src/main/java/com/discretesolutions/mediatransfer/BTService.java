package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BTService {
    private static final String TAG = "BTService";
    private Handler handler;

    private interface BTServiceMessages {
        public static final int READ = 0;
        public static final int WRITE = 1;
        public static final int FEEDBACK_USER = 2;
    }

    private class ThreadedConnection {
        private byte[] buffer;
        private BluetoothSocket btSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ThreadedConnection(BluetoothSocket socket) {
            btSocket = socket;
            InputStream ins = null;
            OutputStream ous = null;
            try {
                ins = btSocket.getInputStream();
                ous = btSocket.getOutputStream();
            } catch (Exception e) {
                Log.e("ThreadedConnection:error:", e.toString());
            } finally {
                inputStream = ins;
                outputStream = ous;
            }
        }

        public void run() {
            buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    Message rmsg = handler.obtainMessage(BTServiceMessages.READ, bytes, -1, buffer);
                    rmsg.sendToTarget();
                } catch (Exception e) {
                    Log.v("BTService:error ", e.toString());
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
                Message wmsg = handler.obtainMessage(BTServiceMessages.WRITE, -1, -1, buffer);
                wmsg.sendToTarget();
            } catch (Exception e) {
                Log.e("Write:Error:", e.toString());
                Message wem = handler.obtainMessage(BTServiceMessages.FEEDBACK_USER);
                Bundle bundle = new Bundle();
                bundle.putString("FEEDBACKMESSAGE", "other device could not receive message");
                wem.setData(bundle);
                handler.sendMessage(wem);
            }
        }

        /*
         *
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


}
