package com.discretesolutions.mediatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Permission;


// Bluetooth profile OPP har verdien 20 i følge
//https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/bluetooth/BluetoothProfile.java

public class BTService {
    private static final String TAG = "BTService";
    private Handler handler;

    public static interface BTServiceMessages {
        public static final int READ = 0;
        public static final int WRITE = 1;
        public static final int FEEDBACK_USER = 2;
    }

    public void profileTest() {
        Log.v("Profile:", "Available");


    }

    public static void sendFile(BluetoothDevice btDevice, String path, Context context) {
        ContentValues values = new ContentValues();
        values.put(BluetoothShare.URI, Uri.fromFile(new File(path)).toString());
        values.put(BluetoothShare.DESTINATION, btDevice.getAddress());
        values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
        long timestamp = System.currentTimeMillis();
        values.put(BluetoothShare.TIMESTAMP, timestamp);
        Uri content = context.getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
    }

    public static void sendFile2(BluetoothDevice btDevice, String path, Context context) {

        ContentValues values = new ContentValues();
        Uri uri = Uri.fromFile(new File(path));
        values.put(BluetoothShare.URI, uri.toString());
        values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
        values.put(BluetoothShare.DESTINATION, btDevice.getAddress());
        long timestamp = System.currentTimeMillis();
        values.put(BluetoothShare.TIMESTAMP, timestamp);
        Intent perm = new Intent();
        perm.setData(uri);
        perm.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String pack = context.getPackageName();
        context.grantUriPermission(pack, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri content = context.getContentResolver().insert(BluetoothShare.CONTENT_URI, values);

    }

    public static void sendFile3(BluetoothDevice btDevice, String[] path, Context context) {
        Intent share = new Intent((Intent.ACTION_SEND));
        share.setType("image/jpeg");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
        for (int i = 0; i < path.length; i++) {
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path[i])));
        }
        context.startActivity(share);
    }


}
